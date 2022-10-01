package com.calmperson.musicplayer.contract

import java.io.FileDescriptor

interface Model {

    interface Audio {
        val source: FileDescriptor
        val name: String
        fun closeFile()
    }

    interface MediaPlayer {
        var playlist: Array<Audio>
        fun play(id: Int)
        fun resume()
        fun pause()
        fun stop()
        fun onStop()
        fun getCurrentPosition() : Int
        fun getDuration(): Int
        fun getAudioName(id: Int): String
    }
}