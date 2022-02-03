package com.poohxx.notelist.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.poohxx.notelist.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var defPref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPref=PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.placeHolder, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            //restricted API
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)finish()
            return super.onOptionsItemSelected(item)

}
    private fun getSelectedTheme():Int{
        return if(defPref.getString("choose_theme_key", "Yellow") =="Yellow"){
            R.style.Theme_NoteListYellow
        }else{
            R.style.Theme_NoteListPurple
        }
    }

}
