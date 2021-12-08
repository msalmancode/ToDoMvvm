package com.example.todomvvm.data;

import androidx.lifecycle.LiveData;
import androidx.room.*
import com.example.todomvvm.data.model.ToDoData

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)
}
