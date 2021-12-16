package com.example.todomvvm.fragment

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.todomvvm.R
import com.example.todomvvm.data.model.Priority
import com.example.todomvvm.data.model.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    var emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkDatabaseIsEmpty(toDoData: List<ToDoData>) {
        emptyDatabase.value = toDoData.isEmpty()
    }

    val listener: AdapterView.OnItemSelectedListener = object :
        AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            when (position) {
                0 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        (ContextCompat.getColor(
                            application,
                            R.color.red
                        ))
                    )
                }
                1 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        (ContextCompat.getColor(
                            application,
                            R.color.yellow
                        ))
                    )

                }
                2 -> {
                    (parent?.getChildAt(0) as TextView).setTextColor(
                        (ContextCompat.getColor(
                            application,
                            R.color.green
                        ))
                    )
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }

    fun verifyDataFromUser(mTitle: String, mDescription: String): Boolean {
        return if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mDescription)) {
            false
        } else !(TextUtils.isEmpty(mTitle)) || !TextUtils.isEmpty(mDescription)
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> {
                Priority.HIGH
            }
            "Medium Priority" -> {
                Priority.MEDIUM
            }
            "Low Priority" -> {
                Priority.LOW
            }
            else -> Priority.LOW
        }
    }

}