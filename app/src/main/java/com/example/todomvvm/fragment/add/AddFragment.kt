package com.example.todomvvm.fragment.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.todomvvm.R

class AddFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        // set menu
        setHasOptionsMenu(true)
        return view;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }
}