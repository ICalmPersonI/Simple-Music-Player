package com.calmperson.musicplayer.presenter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.calmperson.musicplayer.contract.Model
import com.calmperson.musicplayer.contract.Presenter
import com.calmperson.musicplayer.contract.View
import com.calmperson.musicplayer.model.Audio
import com.calmperson.musicplayer.model.MediaPlayerService

class MediaPlayerPresenter(private val view: View.Main) : Presenter.MediaPlayer {

    private lateinit var model: MediaPlayerService
    private var serviceBound: Boolean = false

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

    init {
        Intent(view.context, MediaPlayerService::class.java).also { intent ->
            view.context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun play(id: Int) {
        if (serviceBound) {
            model.play(id)
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

    override fun createPlaylist(directory: Array<String>) {
        val playlist: Array<Model.Audio> = directory.map { Audio(it) }.toTypedArray()
        view.fillPlaylistTable(playlist)
        model.playlist = playlist
    }

    override fun onStop() {
        model.onStop()
    }

}