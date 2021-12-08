package com.example.todomvvm.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
class ToDoData(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var priority: String,
    var description: String

)