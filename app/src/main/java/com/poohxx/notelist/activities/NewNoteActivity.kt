package com.poohxx.notelist.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityNewNoteBinding
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.fragments.NoteFragment
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        actionBarSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save) {
            setMainResult()

        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    private fun setMainResult(){
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, createNewNote())

        }
        setResult(RESULT_OK, i)
        finish()
    }
    private fun createNewNote(): NoteItem{
    return NoteItem(null,
    binding.edTitle.toString(),binding.edDescription.text.toString(),getCurrentTime(),"")
    }
    private fun getCurrentTime(): String{
        val formatter = SimpleDateFormat("hh:mm:ss-yyyy/MM/dd", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)

    }

    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }
}