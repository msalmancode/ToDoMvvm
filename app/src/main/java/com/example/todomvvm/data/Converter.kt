package com.example.todomvvm.data

import androidx.room.TypeConverter
import com.example.todomvvm.data.model.Priority

class Converter {


    fun fromPriority(priority: Priority): String {
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): Priority {
        return Priority.valueOf(priority)
    }
}