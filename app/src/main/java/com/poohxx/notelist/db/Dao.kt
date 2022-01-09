package com.poohxx.notelist.db

import androidx.room.*
import androidx.room.Dao
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListNames
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query("SELECT * FROM list_task_names")
    fun getAllTaskListNames(): Flow<List<TaskListNames>>

    @Query("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertTaskListName(name: TaskListNames)

    @Update
    suspend fun updateNote(note: NoteItem)


}