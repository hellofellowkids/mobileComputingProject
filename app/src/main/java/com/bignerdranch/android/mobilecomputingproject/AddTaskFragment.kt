package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "AddTaskFragment"

class AddTaskFragment : Fragment() {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onBackPressed()
    }

    private var callbacks: Callbacks? = null

    // List view model defined by lazy
    private val addTaskViewModel: AddTaskViewModel by lazy {
        ViewModelProviders.of(this).get(AddTaskViewModel::class.java)
    }


    private lateinit var task: Task

    // widgets on screen
    private lateinit var prioritySelection : Spinner
    private lateinit var customBackButton : ImageView
    private lateinit var confirmNewTask : ImageView
    private lateinit var taskName: EditText
    private lateinit var courseName: EditText

    /* NOT SURE ABOUT THESE AND IF THEY WILL REMAIN TEXTVIEWS */
    private lateinit var addDueDate: TextView
    private lateinit var addDueTime: TextView
    private lateinit var addPersonalDate: TextView
    private lateinit var addPersonalTime: TextView
    private lateinit var addFrequencyText: TextView
    private lateinit var addReminderTime: TextView


    private lateinit var addPrioritySpinner: Spinner




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
        val view = inflater.inflate(R.layout.fragment_add_task, container, false)

        // find widgets in layout
        prioritySelection = view.findViewById(R.id.spinner_priority) as Spinner
        customBackButton = view.findViewById(R.id.back_arrow_new_task) as ImageView
        confirmNewTask = view.findViewById(R.id.confirm_add_task) as ImageView
        taskName = view.findViewById(R.id.added_task_name) as EditText
        courseName = view.findViewById(R.id.subject_name_text) as EditText

        /* AGAIN NOT SURE ABOUT THESE */
        addDueDate = view.findViewById(R.id.add_due_date_text) as TextView
        addDueTime = view.findViewById(R.id.add_due_time_text) as TextView
        addPersonalDate = view.findViewById(R.id.add_personal_date_text) as TextView
        addPersonalTime = view.findViewById(R.id.add_personal_time_text) as TextView
        addFrequencyText = view.findViewById(R.id.add_frequency_text) as TextView
        addReminderTime = view.findViewById(R.id.add_reminder_time_text) as TextView
        addPrioritySpinner = view.findViewById(R.id.spinner_priority) as Spinner

        Log.d(TAG, "New task ID: ${addTaskViewModel.newTask.taskID}")
        // setup spinner
        setupSpinnerAdapter()

        return view
    }

    override fun onStart() {
        super.onStart()

        taskName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                //DO NOTHING
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                task.taskName = sequence.toString()
            }
        })

        courseName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                //DO NOTHING
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                task.courseName = sequence.toString()
            }
        })

        // <-- button
        customBackButton.setOnClickListener {
            // Log.d(TAG, "Back button pressed!")
            callbacks?.onBackPressed()
        }

        // check mark button on top right
        confirmNewTask.setOnClickListener {
            Log.d(TAG, "New task being added!")
            addTaskViewModel.newTask.taskName = "UI Mockup Design"
            addTaskViewModel.newTask.courseName = "Mobile Computing"
            addTaskViewModel.newTask.priority = "High Priority"
            addTaskViewModel.addTask()
            callbacks?.onBackPressed()
        }


        // priority selection
        prioritySelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "Selected item from spinner position = $position")
            }

        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun setupSpinnerAdapter() {
        // Array adapter for task filter
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.priority_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                prioritySelection.adapter = adapter
            }
        }
    }

    companion object {
        fun newInstance(): AddTaskFragment {
            return AddTaskFragment()
        }
    }
}