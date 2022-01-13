package com.poohxx.notelist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityTaskListBinding
import com.poohxx.notelist.db.MainViewModel
import com.poohxx.notelist.entities.TaskListNames

class TaskListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private var taskListName: TaskListNames? = null
    private lateinit var saveItem: MenuItem
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        return true
    }

    private fun expandActionView(): MenuItem.OnActionExpandListener {
        return object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
                return true
            }

        }
    }

    private fun init() {
        taskListName = intent.getSerializableExtra(TASK_LIST_NAME) as TaskListNames
        binding.tvTest.text = taskListName?.name
    }

    companion object {
        const val TASK_LIST_NAME = "task_list_name"
    }
}