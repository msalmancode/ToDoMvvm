package com.example.todomvvm.data.repository

import androidx.lifecycle.LiveData
import com.example.todomvvm.data.TodoDao
import com.example.todomvvm.data.model.ToDoData

class ToDoRepository(private val todoDao: TodoDao) {

    val getAllData: LiveData<List<ToDoData>> = todoDao.getData()
    val sortByHighPriority: LiveData<List<ToDoData>> = todoDao.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> = todoDao.sortByLowPriority()

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

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return todoDao.searchDatabase(searchQuery)
    }

}