package com.poohxx.notelist.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityTaskListBinding
import com.poohxx.notelist.db.MainViewModel
import com.poohxx.notelist.db.TaskListItemAdapter
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames

class TaskListActivity : AppCompatActivity(), TaskListItemAdapter.Listener {
    private lateinit var binding: ActivityTaskListBinding
    private var taskListName: TaskListNames? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: TaskListItemAdapter? = null
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModel.MainViewModelFactory((applicationContext as MainApp).dataBase)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.edNewTaskItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_item) {
            addNewTaskItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewTaskItem() {
        if (edItem?.text.toString().isEmpty()) return
        val item = TaskListItem(
            null,
            edItem?.text.toString(),
            null,
            false,
            taskListName?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertTaskListItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(taskListName?.id!!).observe(this, {
            adapter?.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }

        })
    }

    private fun initRcView() = with(binding) {
        adapter = TaskListItemAdapter(this@TaskListActivity)
        rcView.layoutManager = LinearLayoutManager(this@TaskListActivity)
        rcView.adapter = adapter
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

    }

    companion object {
        const val TASK_LIST_NAME = "task_list_name"
    }



    override fun onClickItem(taskListItem: TaskListItem) {
        mainViewModel.updateListItem(taskListItem)
    }
}