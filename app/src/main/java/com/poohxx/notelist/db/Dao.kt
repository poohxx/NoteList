package com.poohxx.notelist.db

import androidx.room.*
import androidx.room.Dao
import com.poohxx.notelist.entities.LibraryItem
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM list_task_names")
    fun getAllTaskListNames(): Flow<List<TaskListNames>>

    @Query("SELECT * FROM task_list_item WHERE listId LIKE :listId")
    fun getAllTaskListItems(listId: Int): Flow<List<TaskListItem>>

    @Query("SELECT * FROM libraryItem WHERE name LIKE :name")
    suspend fun getAllLibraryItems(name:String): List<LibraryItem>

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query("DELETE FROM list_task_names WHERE id IS :id")
    suspend fun deleteTask(id: Int)

    @Query("DELETE FROM task_list_item WHERE listId LIKE :listId")
    suspend fun deleteTaskItemsByListId(listId: Int)

    @Query("DELETE FROM libraryItem WHERE id LIKE :id")
    suspend fun deleteLibraryItem(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertItem(taskListItem: TaskListItem)

    @Insert
    suspend fun insertLibraryItem(libraryItem: LibraryItem)

    @Insert
    suspend fun insertTaskListName(name: TaskListNames)

    @Update
    suspend fun updateTaskListName(taskListName: TaskListNames)

    @Update
    suspend fun updateListItem(item: TaskListItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateLibraryItem(item: LibraryItem)


}