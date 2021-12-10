package com.example.todomvvm.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.todomvvm.data.model.ToDoData

@Dao
interface TodoDao {

    @Query("Select * From todo_table ORDER BY id ASC")
    fun getData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

}