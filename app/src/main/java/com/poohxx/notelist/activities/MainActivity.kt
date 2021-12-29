package com.poohxx.notelist.activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityMainBinding
import com.poohxx.notelist.fragments.NoteFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBotNavViewListener()
    }

    private fun setBotNavViewListener() {
        binding.btmNavView.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                }
                R.id.new_task -> {
                    Log.d("Log", "new tasl")
                }
                R.id.task_list -> {
                    Log.d("Log", "task_list")
                }
                R.id.notes -> {
                    com.poohxx.notelist.fragments.FragmentManager.setFragment(
                        NoteFragment.newInstance(),
                        this
                    )
                }
            }
            true

        }
    }
}