package com.poohxx.notelist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.poohxx.notelist.entities.LibraryItem
import com.poohxx.notelist.entities.NoteItem
import com.poohxx.notelist.entities.TaskListItem
import com.poohxx.notelist.entities.TaskListNames


@Database (entities = [LibraryItem::class, NoteItem::class, TaskListItem::class, TaskListNames::class], version = 1)
abstract class MainDataBase:RoomDatabase() {
    abstract fun getDao(): Dao

    companion object{
        @Volatile
        private var INSTANCE: MainDataBase? = null
        fun getDataBase(context: Context): MainDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDataBase::class.java,
                    "task_list.db"
                ).build()
                instance
            }
        }
    }
}