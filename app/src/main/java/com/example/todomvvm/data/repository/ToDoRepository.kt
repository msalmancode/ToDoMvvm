package com.example.todomvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.todomvvm.data.TodoDao
import com.example.todomvvm.data.model.ToDoData

class ToDoRepository(private val todoDao: TodoDao) {

    fun getData(): LiveData<List<ToDoData>> = todoDao.getData()

    suspend fun insertData(toDoData: ToDoData) {
        todoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        todoDao.updateData(toDoData)
    }

    suspend fun deleteItem(toDoData: ToDoData) {
        todoDao.deleteData(toDoData)
    }

    suspend fun deleteAll() {
        todoDao.deleteAll()
    }
}