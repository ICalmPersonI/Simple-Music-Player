package com.calmperson.musicplayer.view

import android.app.Activity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.calmperson.musicplayer.R
import com.calmperson.musicplayer.contract.Presenter
import com.calmperson.musicplayer.contract.View
import com.calmperson.musicplayer.presenter.AudioPresenter
import kotlinx.android.synthetic.main.activity_audio.*

class AudioActivity : AppCompatActivity(), View.Audio {
    override lateinit var context: Activity

    private lateinit var presenter: Presenter.Audio

    private var audioId: Int = 0
    private var playlistSize: Int = 0
    private var audioName: String = ""

    private lateinit var audioNameView: TextView
    private lateinit var progressBar: SeekBar
    private lateinit var rewindLeft: ImageButton
    private lateinit var play: ImageButton
    private lateinit var pause: ImageButton
    private lateinit var rewindRight: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        context = this
        presenter = AudioPresenter(this)

        audioId = intent.getIntExtra("audioID", 0)
        playlistSize = intent.getIntExtra("playlistSize", 0)
        audioName = intent.getStringExtra("audioName") ?: ""

        audioNameView = audio_name as TextView
        progressBar = progress_bar as SeekBar
        rewindLeft = rewind_left_button as ImageButton
        play = play_button as ImageButton
        pause = pause_button as ImageButton
        rewindRight = rewind_right_button as ImageButton

        progressBar.progress = 0
        audioNameView.text = audioName
        play.setOnClickListener {
            presenter.play(audioId)
        }
        pause.setOnClickListener {
            presenter.pause()
        }
        rewindLeft.setOnClickListener {
            if (audioId - 1 > -1) {
                audioId--
            } else {
                audioId = playlistSize - 1
            }
            presenter.play(audioId)
        }
        rewindRight.setOnClickListener {
            if (audioId + 1 < playlistSize) {
                audioId++
            } else {
                audioId = 0
            }
            presenter.play(audioId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun setDuration(value: Int) {
        progressBar.max = value
    }

    override fun setCurrentTimePosition(value: Int) {
        progressBar.progress = value
    }

    override fun setAudioName(name: String) {
        audioNameView.text = name
    }

}