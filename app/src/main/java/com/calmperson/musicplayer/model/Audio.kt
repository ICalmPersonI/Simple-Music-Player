package com.calmperson.musicplayer.model

import com.calmperson.musicplayer.contract.Model
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream

class Audio(path: String) : Model.Audio {

    override val source: FileDescriptor
    override val name: String
    private val fileInputStream: FileInputStream

    init {
        val audioTrack: File = File(path)
        this.fileInputStream = FileInputStream(audioTrack)
        this.source = fileInputStream.fd
        this.name = audioTrack.name
    }

    override fun closeFile() {
        fileInputStream.close()
    }
}