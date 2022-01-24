package com.example.todomvvm.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todomvvm.data.ToDoDatabase
import com.example.todomvvm.data.model.Orientation
import com.example.todomvvm.data.model.ToDoData
import com.example.todomvvm.data.repository.ToDoRepository
import com.example.todomvvm.utils.DataStoreManager
import com.example.todomvvm.utils.PrefKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private var dataStore = DataStoreManager(application)

    private val todoDao = ToDoDatabase.getDatabase(application).todoDao()

    private val repository: ToDoRepository = ToDoRepository(todoDao)
    val getAllData: LiveData<List<ToDoData>> = repository.getAllData
    val sortByHighPriority: LiveData<List<ToDoData>> = repository.sortByHighPriority
    val sortByLowPriority: LiveData<List<ToDoData>> = repository.sortByLowPriority

    var recyclerViewOrientation: MutableLiveData<String> = MutableLiveData(Orientation.LINEAR.name)

    init {
        // Getting Data from DataStore preference
        getOrientation()
    }

    fun saveOrientation(orientation: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveString(PrefKey.ORIENTATION, orientation)
        }
    }

    private fun getOrientation() {
        viewModelScope.launch(Dispatchers.Main) {
            dataStore.getString(PrefKey.ORIENTATION).collect {
                recyclerViewOrientation.value = it
            }
        }
    }

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateDate(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteItem(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return repository.searchDatabase(searchQuery)
    }
}
