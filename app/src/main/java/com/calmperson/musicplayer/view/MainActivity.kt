package com.calmperson.musicplayer.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.calmperson.musicplayer.Constants
import com.calmperson.musicplayer.R
import com.calmperson.musicplayer.contract.Model
import com.calmperson.musicplayer.contract.Presenter
import com.calmperson.musicplayer.presenter.MediaPlayerPresenter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.playlist_table_row.view.*
import java.io.File


class MainActivity : AppCompatActivity(), com.calmperson.musicplayer.contract.View.Main {
    override lateinit var context: Activity

    private lateinit var presenter: Presenter.MediaPlayer

    private lateinit var fileBrowserLauncher: ActivityResultLauncher<Intent>

    private lateinit var firstStart: LinearLayout
    private lateinit var tableScroll: ScrollView
    private lateinit var playlistTable: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        requestPermissions()

        presenter = MediaPlayerPresenter(this)
        fileBrowserLauncher = initFileBrowserLauncher()

        firstStart = first_start as LinearLayout
        tableScroll = table_scroll as ScrollView
        playlistTable = playlist_table as TableLayout

        firstStart.setOnClickListener {
            val intent: Intent = Intent(this, FolderBrowserActivity::class.java)
            fileBrowserLauncher.launch(intent)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onStop()
    }

    override fun fillPlaylistTable(playlist: Array<Model.Audio>) {
        playlistTable.removeAllViews()
        for (i: Int in playlist.indices) {
            val audio: Model.Audio = playlist[i]
            val tableRow: TableRow = this.layoutInflater.inflate(R.layout.playlist_table_row, null) as TableRow
            tableRow.name.text = audio.name
            tableRow.play_button.setOnClickListener {
                presenter.play(i)
            }
            tableRow.pause_button.setOnClickListener {
                presenter.pause()
            }
            tableRow.name.setOnClickListener {
                val intent: Intent = Intent(this, AudioActivity::class.java)
                intent.putExtra("audioID", i)
                intent.putExtra("playlistSize", playlist.size)
                intent.putExtra("audioName", audio.name)
                startActivity(intent)
            }
            playlistTable.addView(tableRow)
        }
    }

    private fun requestPermissions() {
        val permissions: Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MEDIA_CONTENT_CONTROL,
            Manifest.permission.READ_PHONE_STATE
        )
        for (permission: String in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 1)
            }
        }
    }

    private fun initFileBrowserLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            val selectedFolder: String? = result.data?.extras?.getString("selectedFolder")
            selectedFolder?.let { folder ->
                File(folder).list()?.let { list ->
                    val directory: Array<String> = list
                        .filter { Constants.AUDIO_FORMATS.contains(it.split('.').last()) }
                        .map { folder + File.separator + it }.toTypedArray()
                    if (directory.isNotEmpty()) {
                        firstStart.visibility = View.GONE
                        tableScroll.visibility = View.VISIBLE
                        presenter.createPlaylist(directory)
                    }
                }
            }
        }
    }
}