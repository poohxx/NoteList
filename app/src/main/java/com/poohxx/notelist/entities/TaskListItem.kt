package com.poohxx.notelist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list_item")
data class TaskListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "ItemInfo")
    val itemInfo: String = "",
    @ColumnInfo(name = "itemChecked")
    val itemChecked: Boolean = false,
    @ColumnInfo(name = "listId")
    val listID: Int,
    @ColumnInfo(name = "itemType")
    val itemType: Int = 0
)