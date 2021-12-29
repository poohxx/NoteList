package com.poohxx.notelist.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libraryItem")
class LibraryItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "name")
    val name: String
)
