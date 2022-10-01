package com.calmperson.musicplayer.contract

import android.app.Activity

interface View {

    interface Main {
        val context: Activity
        fun fillPlaylistTable(playlist: Array<Model.Audio>)
    }

    interface Audio {
        val context: Activity
        fun setDuration(value: Int)
        fun setCurrentTimePosition(value: Int)
        fun setAudioName(name: String)
    }

}