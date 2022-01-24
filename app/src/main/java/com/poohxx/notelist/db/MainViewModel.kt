package com.poohxx.notelist.db

import androidx.lifecycle.*
import com.poohxx.notelist.entities.LibraryItem
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(dataBase: MainDataBase) : ViewModel() {
    val dao = dataBase.getDao()
    val libraryItems = MutableLiveData<List<LibraryItem>>()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allTaskListNames: LiveData<List<TaskListNames>> = dao.getAllTaskListNames().asLiveData()
    fun getAllItemsFromList(listId: Int): LiveData<List<TaskListItem>> {
        return dao.getAllTaskListItems(listId).asLiveData()
    }

    fun getAllLibraryItems(name: String)= viewModelScope.launch  {
       libraryItems.postValue(dao.getAllLibraryItems(name))}

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertTaskListName(listName: TaskListNames) = viewModelScope.launch {
        dao.insertTaskListName(listName)
    }

    fun insertTaskListItem(taskListItem: TaskListItem) = viewModelScope.launch {
        dao.insertItem(taskListItem)
        if(isLibraryItemExists(taskListItem.name))dao.insertLibraryItem(LibraryItem(null, taskListItem.name))
    }

    fun updateListItem(item: TaskListItem) = viewModelScope.launch {
        dao.updateListItem(item)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }
    fun updateLibraryItem(item: LibraryItem) = viewModelScope.launch {
        dao.updateLibraryItem(item)
    }

    fun updateTaskListName(taskListName: TaskListNames) = viewModelScope.launch {
        dao.updateTaskListName(taskListName)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }
    fun deletelibraryItem(id: Int) = viewModelScope.launch {
        dao.deleteLibraryItem(id)
    }

    fun deleteTaskList(id: Int, deleteList: Boolean) = viewModelScope.launch {
        if(deleteList) dao.deleteTask(id)
        dao.deleteTaskItemsByListId(id)
    }
    private suspend fun isLibraryItemExists(name:String):Boolean{
        return dao.getAllLibraryItems(name).isEmpty()
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
