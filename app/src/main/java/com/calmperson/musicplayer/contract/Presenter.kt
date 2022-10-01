package com.calmperson.musicplayer.contract

interface Presenter {
    interface MediaPlayer {
        fun play(id: Int)
        fun pause()
        fun resume()
        fun stop()
        fun createPlaylist(directory: Array<String>)
        fun onStop()
    }

    interface Audio {
        fun play(id: Int)
        fun pause()
        fun resume()
        fun stop()
        fun getAudioName(id: Int): String
        fun onStop()
    }
}