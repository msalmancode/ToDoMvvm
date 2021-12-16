package com.example.todomvvm.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todomvvm.data.model.ToDoData

@Dao
interface TodoDao {

    @Query("Select * From todo_table ORDER BY id ASC")
    fun getData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteData(toDoData: ToDoData)

    @Query("Delete from todo_table")
    suspend fun deleteAll()

}