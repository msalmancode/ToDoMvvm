package com.example.todomvvm.fragment.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todomvvm.R
import com.example.todomvvm.data.model.Orientation
import com.example.todomvvm.data.model.ToDoData
import com.example.todomvvm.data.viewmodel.ToDoViewModel
import com.example.todomvvm.databinding.FragmentListBinding
import com.example.todomvvm.fragment.SharedViewModel
import com.example.todomvvm.fragment.list.adapter.ListAdapter
import com.example.todomvvm.utils.DataStoreManager
import com.example.todomvvm.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar


class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val todoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var dataStore: DataStoreManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Data Binding
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mSharedViewModel = sharedViewModel

        dataStore = DataStoreManager(requireContext())

        // setup Recycler View
        setUpRecyclerView()

        // Observing live data
        getLiveListData()

        // set menu
        setHasOptionsMenu(true)

        // hide soft keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.listRecyclerView
        recyclerView.adapter = adapter
        todoViewModel.recyclerViewOrientation.observe(viewLifecycleOwner, {
            when (it) {
                Orientation.LINEAR.name -> {
                    recyclerView.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                }
                Orientation.GRID.name -> {
                    recyclerView.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
            }
        })
        // swipe to delete
        swipeToDelete(recyclerView)
    }


    private fun getLiveListData() {
        todoViewModel.getAllData.observe(viewLifecycleOwner, { data ->
            sharedViewModel.checkDatabaseIsEmpty(data)
            adapter.setData(data)
        })
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                // Delete Item
                todoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedItem(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedItem(view: View, toDoData: ToDoData) {
        val snackBar = Snackbar.make(
            view, "Deleted : '${toDoData.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            todoViewModel.insertData(toDoData)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        // set search menu
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_menu -> confirmRemoval()
            R.id.menu_arrange -> {
                if (todoViewModel.recyclerViewOrientation.value == Orientation.GRID.name) {
                    todoViewModel.saveOrientation(Orientation.LINEAR.name)
                    todoViewModel.recyclerViewOrientation.value = Orientation.LINEAR.name
                } else {
                    todoViewModel.saveOrientation(Orientation.GRID.name)
                    todoViewModel.recyclerViewOrientation.value = Orientation.GRID.name
                }
                getLiveListData()
            }
            R.id.sort_by_menu -> {
                sortList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query = query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query = query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"
        todoViewModel.searchDatabase(searchQuery).observe(this, { list ->
            list.let {
                adapter.setData(it)
            }
        })
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

    private fun sortList() {
        val list = arrayOf("Priority High", "Priority Low")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Sort By")
        var dialog = builder.create()
        builder.setSingleChoiceItems(list, -1) { _, which: Int ->
            when (which) {
                0 -> {
                    todoViewModel.sortByHighPriority.observe(
                        this,
                        { adapter.setData(it) })
                }
                1 ->
                    todoViewModel.sortByLowPriority.observe(
                        this,
                        { adapter.setData(it) })
            }
            dialog.dismiss()
        }
        dialog = builder.create()
        dialog.show()
    }
}