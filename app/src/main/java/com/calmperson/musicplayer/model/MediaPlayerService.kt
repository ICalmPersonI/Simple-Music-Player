package com.calmperson.musicplayer.model

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.calmperson.musicplayer.contract.Model


class MediaPlayerService : Service(), Model.MediaPlayer {

    override var playlist: Array<Model.Audio> = arrayOf()
    private lateinit var player: MediaPlayer
    var currAudioID: Int = -1
    private var resumePosition: Int = 0
    private val iBinder: IBinder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder = iBinder

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
        player.release()
        onStop()
    }

    override fun play(id: Int) {
        if (currAudioID == id) {
            resume()
        } else {
            player.reset()
            currAudioID = id
            player.setDataSource(playlist[id].source)
            player.prepare()
            player.start()
        }
    }

    override fun resume() {
        if (!player.isPlaying) {
            player.seekTo(resumePosition)
            player.start()
        }
    }

    override fun pause() {
        if (player.isPlaying) {
            player.pause()
            resumePosition = player.currentPosition
        }
    }

    override fun stop() {
        if (player.isPlaying) {
            player.stop()
            player.reset()
        }
    }

    override fun onStop() {
        playlist.forEach { it.closeFile() }
    }

    override fun getCurrentPosition(): Int = player.currentPosition

    override fun getDuration(): Int = if (player.isPlaying) player.duration else 0

    override fun getAudioName(id: Int): String = playlist[id].name

    private fun initMediaPlayer() {
        player = MediaPlayer()
        player.setOnErrorListener { mp, what, extra ->
            when (what) {
                MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK -> Log.d(
                    "MediaPlayer Error",
                    "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK $extra"
                )
                MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.d(
                    "MediaPlayer Error",
                    "MEDIA ERROR SERVER DIED $extra"
                )
                MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.d(
                    "MediaPlayer Error",
                    "MEDIA ERROR UNKNOWN $extra"
                )
            }
            false
        }
        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
    }

    inner class LocalBinder : Binder() {
        val service: MediaPlayerService
            get() = this@MediaPlayerService
    }
}