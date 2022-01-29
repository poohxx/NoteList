package com.poohxx.notelist.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.poohxx.notelist.R
import com.poohxx.notelist.databinding.ActivityTaskListBinding
import com.poohxx.notelist.db.MainViewModel
import com.poohxx.notelist.db.TaskListItemAdapter
import com.poohxx.notelist.dialogs.EditListItemDialog
import com.poohxx.notelist.entities.LibraryItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames
import com.poohxx.notelist.utils.ShareHelper

class TaskListActivity : AppCompatActivity(), TaskListItemAdapter.Listener {
    private lateinit var binding: ActivityTaskListBinding
    private var taskListName: TaskListNames? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: TaskListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher
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
        textWatcher = textWatcher()
        return true
    }

    private fun textWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("MyLog", "On Text Changed: $p0")
                mainViewModel.getAllLibraryItems("%$p0%")

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {
                addNewTaskItem(edItem?.text.toString())
            }
            R.id.delete_list -> {
                mainViewModel.deleteTaskList(taskListName?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteTaskList(taskListName?.id!!, false)

            }
            R.id.share_list -> {
                startActivity(
                    Intent.createChooser(
                        ShareHelper.shareTaskList(adapter?.currentList!!, taskListName?.name!!),
                        "Share by"
                    )
                )

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addNewTaskItem(name: String) {
        if (name.isEmpty()) return
        val item = TaskListItem(
            null,
            name,
            "",
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

    private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this, {
            val tempTaskList = ArrayList<TaskListItem>()
            it.forEach { item ->
                val taskItem = TaskListItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempTaskList.add(taskItem)
            }
            adapter?.submitList(tempTaskList)
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
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(taskListName?.id!!)
                    .removeObservers(this@TaskListActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@TaskListActivity)
                edItem?.setText("")
                listItemObserver()
                libraryItemObserver()

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


    override fun onClickItem(taskListItem: TaskListItem, state: Int) {
        when (state) {
            TaskListItemAdapter.CHECK_BOX -> mainViewModel.updateListItem(taskListItem)
            TaskListItemAdapter.EDIT -> editListItem(taskListItem)
            TaskListItemAdapter.EDIT_LIBRARY_ITEM -> editLibraryItem(taskListItem)
            TaskListItemAdapter.ADD -> addNewTaskItem(taskListItem.name)
            TaskListItemAdapter.DELETE_LIBRARY_ITEM -> {
                mainViewModel.deleteLibraryItem(taskListItem.id!!)
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }

        }
        mainViewModel.updateListItem(taskListItem)
    }

    private fun editListItem(item: TaskListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: TaskListItem) {
                mainViewModel.updateListItem(item)
            }

        })

    }

    private fun editLibraryItem(item: TaskListItem) {
        EditListItemDialog.showDialog(this, item, object : EditListItemDialog.Listener {
            override fun onClick(item: TaskListItem) {
                mainViewModel.updateLibraryItem(LibraryItem(item.id, item.name))
                mainViewModel.getAllLibraryItems("%${edItem?.text.toString()}%")
            }

        })

    }


}
