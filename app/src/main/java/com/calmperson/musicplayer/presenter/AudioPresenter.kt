package com.calmperson.musicplayer.presenter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.calmperson.musicplayer.contract.Presenter
import com.calmperson.musicplayer.contract.View
import com.calmperson.musicplayer.model.MediaPlayerService

class AudioPresenter(private val view: View.Audio) : Presenter.Audio {
    private lateinit var model: MediaPlayerService
    private var serviceBound: Boolean = false
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.LocalBinder
            model = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceBound = false
        }
    }

    private val timePositionUpdater = object : Runnable {
        override fun run() {
            view.setCurrentTimePosition(model.getCurrentPosition())
            handler.postDelayed(this, 100)
        }
    }

    init {
        Intent(view.context, MediaPlayerService::class.java).also { intent ->
            view.context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
        handler.postDelayed(timePositionUpdater, 100)
    }

    override fun play(id: Int) {
        if (serviceBound) {
            model.play(id)
            view.setDuration(model.getDuration())
            view.setAudioName(model.getAudioName(id))
        }
    }

    override fun pause() {
        if (serviceBound) {
            model.pause()
        }
    }

    override fun resume() {
        if (serviceBound) {
            model.resume()
        }
    }

    override fun stop() {
        if (serviceBound) {
            model.stop()
        }
    }

    override fun getAudioName(id: Int): String {
        if (serviceBound) {
            return model.getAudioName(id)
        }
        return "Name"
    }

    override fun onStop() {
        handler.removeCallbacks(timePositionUpdater)
    }
}