package com.poohxx.notelist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "List_task_names")
data class TaskListNames(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "allItemCounter")
    val allItemCounter: Int,
    @ColumnInfo(name = "CheckedItemCounter")
    val checkedItemCounter: Int,
    @ColumnInfo(name = "ItemIds")
    val itemIds: String
) : Serializable
