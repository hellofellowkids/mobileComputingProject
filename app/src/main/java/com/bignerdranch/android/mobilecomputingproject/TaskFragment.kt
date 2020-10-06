package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import java.util.*

private const val TAG = "TaskFragment"
private const val ARG_TASK_ID = "task_id"

class TaskFragment : Fragment() {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onBackArrow()
    }

    private var callbacks: Callbacks? = null

    // view model defined by lazy
    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    // define widgets in layout
    private lateinit var backArrow : ImageView
    private lateinit var deleteIcon : ImageView

    // local task
    private lateinit var task : Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val taskID: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        Log.d(TAG, "args bundle task ID: $taskID")

        // Load task from database
        taskDetailViewModel.load(taskID)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate task_list view
        val view = inflater.inflate(R.layout.fragment_task_view, container, false)

        // find widgets in layout
        backArrow = view.findViewById(R.id.back_arrow) as ImageView
        deleteIcon = view.findViewById(R.id.delete_icon) as ImageView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer { task ->
                task?.let {
                    this.task = task
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        // set widget with info from 'task' var
    }

    override fun onStart() {
        super.onStart()

        // <-- button
        backArrow.setOnClickListener {
            callbacks?.onBackArrow()
        }

        // delete icon on top right
        deleteIcon.setOnClickListener {
            taskDetailViewModel.deleteTask(task)
            callbacks?.onBackArrow()
        }
    }

    override fun onStop() {
        super.onStop()
        // taskDetailViewModel.saveTask // save info of modified task
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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