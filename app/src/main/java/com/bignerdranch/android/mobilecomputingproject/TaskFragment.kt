package com.bignerdranch.android.mobilecomputingproject

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.util.*

private const val TAG = "TaskFragment"
private const val ARG_TASK_ID = "task_id"

class TaskFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskID: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        Log.d(TAG, "args bundle crime ID: $taskID")
        // Eventually, load crime from database
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate task_list view
        val view = inflater.inflate(R.layout.fragment_task_view, container, false)

        // find widgets in layout

        return view
    }


    companion object {

        fun newInstance(taskId: UUID): TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }
}