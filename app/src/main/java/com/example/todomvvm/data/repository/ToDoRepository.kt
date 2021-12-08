package com.example.todomvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.todomvvm.data.TodoDao
import com.example.todomvvm.data.model.ToDoData


class ToDoRepository(private val toDoDao: TodoDao) {

    var getAllData: LiveData<List<ToDoData>> = toDoDao.getAllData()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }
}