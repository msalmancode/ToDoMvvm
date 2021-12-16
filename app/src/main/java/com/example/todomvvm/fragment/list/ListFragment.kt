package com.example.todomvvm.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todomvvm.R
import com.example.todomvvm.data.viewmodel.ToDoViewModel
import com.example.todomvvm.databinding.FragmentListBinding
import com.example.todomvvm.fragment.SharedViewModel
import com.example.todomvvm.fragment.list.adapter.ListAdapter


class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!


    private val todoViewModel: ToDoViewModel by viewModels()

    private val sharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Data Binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = sharedViewModel

        // setup Recycler View
        setUpRecyclerView()


        // Observing live data
        todoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkDatabaseIsEmpty(data)
            adapter.setData(data)
        })


        // set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.listRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_menu -> confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            todoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully Removed Everything!",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove Everything?")
        builder.create().show()

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}