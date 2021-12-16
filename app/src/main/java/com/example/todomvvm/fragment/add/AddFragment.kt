package com.example.todomvvm.fragment.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.todomvvm.R
import com.example.todomvvm.data.model.ToDoData
import com.example.todomvvm.data.viewmodel.ToDoViewModel
import com.example.todomvvm.databinding.FragmentAddBinding
import com.example.todomvvm.fragment.SharedViewModel

class AddFragment : Fragment() {

    private lateinit var addBinding: FragmentAddBinding

    private val todoViewModel: ToDoViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addBinding = FragmentAddBinding.inflate(inflater, container, false)

        // setup spinner Listener
        addBinding.prioritySpinner.onItemSelectedListener = sharedViewModel.listener

        // set menu
        setHasOptionsMenu(true)

        return addBinding.root;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_menu) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = addBinding.titleEt.text.toString()
        val mPriority = addBinding.prioritySpinner.selectedItem.toString()
        val mDescription = addBinding.descriptionEt.text.toString()

        val isValidate = sharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if (isValidate) {

            val data = ToDoData(
                0,
                mTitle,
                sharedViewModel.parsePriority(mPriority),
                mDescription
            )

            todoViewModel.insertData(data)
            Toast.makeText(context, "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)

        } else {
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }

    }


}