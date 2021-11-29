package com.example.todomvvm.fragment.list

import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todomvvm.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            .setOnClickListener(View.OnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_addFragment)
            })

        view.findViewById<ConstraintLayout>(R.id.layoutConstraint)
            .setOnClickListener(View.OnClickListener {
                findNavController().navigate(R.id.action_listFragment_to_updateFragment)
            })

        // set menu
        setHasOptionsMenu(true)

        return view


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }
}