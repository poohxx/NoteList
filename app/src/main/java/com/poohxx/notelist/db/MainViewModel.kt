package com.poohxx.notelist.db

import androidx.lifecycle.*
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(dataBase: MainDataBase) : ViewModel() {
    val dao = dataBase.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allTaskListNames: LiveData<List<TaskListNames>> = dao.getAllTaskListNames().asLiveData()
    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertTaskListName(listName: TaskListNames) = viewModelScope.launch {
        dao.insertTaskListName(listName)
    }
    fun insertTaskListItem(taskListItem: TaskListItem) = viewModelScope.launch {
        dao.insertItem(taskListItem)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }
    fun updateTaskListName(taskListName: TaskListNames) = viewModelScope.launch {
        dao.updateTaskListName(taskListName)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

        fun deleteTask(id: Int) = viewModelScope.launch {
            dao.deleteTask(id)
        }

        class MainViewModelFactory(val dataBase: MainDataBase) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return MainViewModel(dataBase) as T
                }
                throw IllegalArgumentException("Unknown ViewModelClass")
            }

        }

    }
