package com.calmperson.musicplayer.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.calmperson.musicplayer.Constants
import com.calmperson.musicplayer.R
import kotlinx.android.synthetic.main.activity_folder_browser.*
import kotlinx.android.synthetic.main.folder_table_row.view.*
import java.io.File
import java.util.*


class FolderBrowserActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var confirmButton: Button
    private lateinit var foldersTable: TableLayout

    private val foldersHistory: LinkedList<File> = LinkedList()

    private var currFolder: File = Environment.getExternalStorageDirectory()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_browser)

        backButton = back_button as Button
        confirmButton = confirm_button as Button
        foldersTable = folders_table as TableLayout

        confirmButton.setOnClickListener {
            val data: Intent = Intent()
            data.putExtra("selectedFolder", currFolder.toString())
            this.setResult(Activity.RESULT_OK, data)
            this.finish()
        }

        backButton.setOnClickListener {
            foldersHistory.removeLast()
            if (foldersHistory.isEmpty()) {
                this.finish()
            } else {
                currFolder = foldersHistory.removeLast()
                fillTable(currFolder)
            }
        }
        fillTable(currFolder)
    }

    private fun fillTable(folderPath: File) {
        currFolder = folderPath
        foldersHistory.add(folderPath)
        foldersTable.removeAllViews()
        val folders: Array<String> = folderPath.list()?.filter { name ->
                val parted = name.split('.')
                parted.size == 1 || Constants.AUDIO_FORMATS.contains(parted.last())
            }?.toTypedArray() ?: emptyArray()
        for (i: Int in folders.indices) {
            val folder: String = folders[i]
            val row: TableRow = layoutInflater.inflate(R.layout.folder_table_row, null) as TableRow
            val folderName: TextView = row.folder_name
            row.folder_name.setOnClickListener {
                val nextFolder: File = File(currFolder.toString() + File.separator + folder)
                fillTable(nextFolder)
            }
            folderName.text = folder
            foldersTable.addView(row)
        }
    }
}
