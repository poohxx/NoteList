package com.poohxx.notelist.activities

import TaskListNamesFragment
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityMainBinding
import com.poohxx.notelist.dialogs.NewListDialog
import com.poohxx.notelist.fragments.FragmentManager
import com.poohxx.notelist.fragments.NoteFragment
import com.poohxx.notelist.settings.SettingsActivity

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    private var currentMenuItemId = R.id.task_list
    private lateinit var defPref: SharedPreferences
    private var currentTheme = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        defPref = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        currentTheme = defPref.getString("choose_theme_key", "Yellow").toString()
        setContentView(binding.root)
        FragmentManager.setFragment(
            TaskListNamesFragment.newInstance(),
            this
        )
        setBotNavViewListener()
    }

    private fun setBotNavViewListener() {
        binding.btmNavView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.new_task -> {
                    FragmentManager.currentFrag?.onClickNew()

                }
                R.id.task_list -> {
                    currentMenuItemId = R.id.task_list
                    FragmentManager.setFragment(
                        TaskListNamesFragment.newInstance(),
                        this
                    )
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(
                        NoteFragment.newInstance(),
                        this
                    )
                }
            }
            true

        }

    }

    override fun onResume() {
        super.onResume()
        binding.btmNavView.selectedItemId = currentMenuItemId
        if (defPref.getString("choose_theme_key", "Yellow") != currentTheme) recreate()
    }

    private fun getSelectedTheme(): Int {
        return if (defPref.getString("choose_theme_key", "Yellow") == "Yellow") {
            R.style.Theme_NoteListYellow
        } else {
            R.style.Theme_NoteListPurple
        }
    }

    override fun onClick(name: String) {
        Toast.makeText(this, "Name: $name", Toast.LENGTH_SHORT).show()
    }
}