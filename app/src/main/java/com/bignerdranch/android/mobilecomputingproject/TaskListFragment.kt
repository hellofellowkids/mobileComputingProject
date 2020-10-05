package com.bignerdranch.android.mobilecomputingproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TaskListFragment : Fragment() {

    // widgets inside task list
    private lateinit var taskRecycleView : RecyclerView
    private lateinit var addTaskButton : FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate task_list view
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        // find widgets in layout
        taskRecycleView = view.findViewById(R.id.task_recycler_view) as RecyclerView
        addTaskButton = view.findViewById(R.id.add_task_button) as FloatingActionButton

        return view;
    }

    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }
}