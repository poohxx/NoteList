package com.poohxx.notelist.activities

import TaskListNamesFragment
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityMainBinding
import com.poohxx.notelist.dialogs.NewListDialog
import com.poohxx.notelist.fragments.FragmentManager
import com.poohxx.notelist.fragments.NoteFragment

class MainActivity : AppCompatActivity(), NewListDialog.Listener {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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
                    Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                }
                R.id.new_task -> {
                    FragmentManager.currentFrag?.onClickNew()

                }
                R.id.task_list -> {
                    FragmentManager.setFragment(
                        TaskListNamesFragment.newInstance(),
                        this
                    )
                }
                R.id.notes -> {
                    FragmentManager.setFragment(
                        NoteFragment.newInstance(),
                        this
                    )
                }
            }
            true

        }

    }

    override fun onClick(name: String) {
        Toast.makeText(this, "Name: $name", Toast.LENGTH_SHORT).show()
    }
}