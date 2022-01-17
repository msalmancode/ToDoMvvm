package com.example.todomvvm.fragment.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todomvvm.R
import com.example.todomvvm.data.model.ToDoData
import com.example.todomvvm.data.viewmodel.ToDoViewModel
import com.example.todomvvm.databinding.FragmentUpdateBinding
import com.example.todomvvm.fragment.SharedViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // set Data Binding
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.currentData = args.currentItem

        // setup Spinner Listener
        binding.currentPrioritySpinner.onItemSelectedListener = sharedViewModel.listener

        // set menu
        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    //show alert dialog to confirm item removal
    private fun confirmItemRemoval() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Successfully Removed: ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete '${args.currentItem.title}'?")
        builder.setMessage("Are you sure you want to remove '${args.currentItem.title}'?")
        builder.create().show()

    }


    private fun updateItem() {

        val title = binding.currentTitleEt.text.toString()
        val priority = binding.currentPrioritySpinner.selectedItem.toString()
        val description = binding.currentDescriptionEt.text.toString()

        if (sharedViewModel.verifyDataFromUser(title, description)) {

            val toDoData = ToDoData(
                args.currentItem.id,
                title,
                sharedViewModel.parsePriority(priority),
                description
            )
            todoViewModel.updateDate(toDoData)
            Toast.makeText(context, "Successfully updated!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding= null
    }
}