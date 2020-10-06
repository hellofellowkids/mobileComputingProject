package com.bignerdranch.android.mobilecomputingproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AddTaskFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate task_list view
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        // find widgets in layout

        return view
    }

    companion object {
        fun newInstance(): AddTaskFragment {
            return AddTaskFragment()
        }
    }
}