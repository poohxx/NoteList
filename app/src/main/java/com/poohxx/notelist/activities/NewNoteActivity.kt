package com.poohxx.notelist.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.method.TextKeyListener.clear
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityNewNoteBinding
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.fragments.NoteFragment
import com.poohxx.notelist.utils.HtmlManager
import com.poohxx.notelist.utils.MyTouchListener
import com.poohxx.notelist.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewNoteBinding
    private var note: NoteItem? = null
    private var pref:SharedPreferences?=null
    private lateinit var defPref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityNewNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        defPref=PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        setTextSize()
        onClickColorPicker()
        actionMenuCallback()
    }
    private fun onClickColorPicker() = with(binding){
        imBtnRed.setOnClickListener { setColorForSelectedText(R.color.picker_red) }
        imBtnOrange.setOnClickListener { setColorForSelectedText(R.color.picker_orange) }
        imBtnYellow.setOnClickListener { setColorForSelectedText(R.color.picker_yellow) }
        imBtnGreen.setOnClickListener { setColorForSelectedText(R.color.picker_green) }
        imBtnBlue.setOnClickListener { setColorForSelectedText(R.color.picker_blue) }
        imBtnPurple.setOnClickListener { setColorForSelectedText(R.color.picker_purple) }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun init(){
        binding.colorPicker.setOnTouchListener(MyTouchListener())
        pref=PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = sNote as NoteItem
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
        edTitle.setText(note?.title)
        edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
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
        } else if (item.itemId == R.id.id_bold) {
            setBoldForSelectedText()
      // } else if (item.itemId == R.id.id_color) {
       //  if (binding.colorPicker.isShown) {
        //        closeColorPicker()
        //  } else {
       //       openColorPicker()
      //    }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd
        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }
    private fun setColorForSelectedText(colorID: Int) = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd
        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)

        if (styles.isNotEmpty()) edDescription.text.removeSpan(styles[0])


        edDescription.text.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    this@NewNoteActivity,
                    colorID
                )
            ), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val i = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, i)
        finish()
    }

    private fun updateNote(): NoteItem? = with(binding) {
        return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text)
        )
    }

    private fun createNewNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text),
            TimeManager.getCurrentTime(), ""
        )
    }



    private fun actionBarSettings() {
        val ab = supportActionBar
        ab?.setDisplayHomeAsUpEnabled(true)
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

        binding.colorPicker.startAnimation(openAnim)
    }
    private fun actionMenuCallback(){
        val actionCallback = object :ActionMode.Callback{
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return true

            }

            override fun onDestroyActionMode(p0: ActionMode?) {
                            }

        }
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }
    private fun setTextSize()=with(binding){
        edTitle.setTextSize(pref?.getString("title_size_key", "14"))
        edDescription.setTextSize(pref?.getString("content_size_key", "14"))
    }
    private fun EditText.setTextSize(size: String?){
        if(size!=null) this.textSize = size.toFloat()
    }
    private fun getSelectedTheme():Int{
        return if(defPref.getString("choose_theme_key", "Yellow") =="Yellow"){
            R.style.Theme_NewNoteListYellow
        }else{
            R.style.Theme_NewNoteListPurple
        }
    }
}