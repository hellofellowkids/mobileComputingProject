package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import org.w3c.dom.Text
import java.util.*

private const val TAG = "TaskFragment"
private const val ARG_TASK_ID = "task_id"

class TaskFragment : Fragment() {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onBackArrow()
        fun onEditSelected(taskId: UUID)
    }

    private var callbacks: Callbacks? = null

    // view model defined by lazy
    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    // define widgets in layout
    private lateinit var backArrow : ImageView
    private lateinit var deleteIcon : ImageView
    private lateinit var taskName: TextView
    private lateinit var dueDate: TextView
    private lateinit var dueTime: TextView
    private lateinit var personalDate: TextView
    private lateinit var personalTime: TextView
    private lateinit var priority: TextView
    private lateinit var reminder: TextView
    private lateinit var subjectName: TextView
    private lateinit var editSettingsButton: ImageButton
    private lateinit var checkButton: ImageButton


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
        taskName = view.findViewById(R.id.task_name) as TextView
        dueDate = view.findViewById(R.id.due_date_text) as TextView
        dueTime = view.findViewById(R.id.due_time_text) as TextView
        personalDate = view.findViewById(R.id.personal_date_text) as TextView
        personalTime = view.findViewById(R.id.personal_time_text) as TextView
        priority = view.findViewById(R.id.priority_text) as TextView
        reminder = view.findViewById(R.id.reminder_text) as TextView
        subjectName = view.findViewById(R.id.subject_text) as TextView
        editSettingsButton = view.findViewById(R.id.edit_settings_button) as ImageButton
        checkButton = view.findViewById(R.id.check_button) as ImageButton

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
        taskName.text = task.taskName
        dueDate.text = task.finalDeadlineDate.toString()
        dueTime.text = task.finalDeadlineTime.toString()
        personalDate.text = task.personalDeadlineDate.toString()
        personalTime.text = task.personalDeadlineTime.toString()
        priority.text = task.priority
        reminder.text = task.reminderFrequency.toString() //This is the date when the notification will go out
        subjectName.text = task.courseName

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

        editSettingsButton.setOnClickListener {
            //We want to edit this task
            Log.d(TAG, "Edit Settings Button Clicked")
            //callbacks?.onEditSelected(taskId)

        }

        checkButton.setOnClickListener {
            //We want to complete this task
            Log.d(TAG, "Complete Button Clicked")
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