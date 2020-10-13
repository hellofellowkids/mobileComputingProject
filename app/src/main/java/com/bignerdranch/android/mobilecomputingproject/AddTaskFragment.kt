package com.bignerdranch.android.mobilecomputingproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*


private const val TAG = "AddTaskFragment"
private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATE = 0
private const val REQUEST_TIME = 1
private const val ARG_EDIT_TASK_ID = "editingID"

class AddTaskFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onBackPressed()
        fun onSetReminder(reminderTime : Date, taskName : String)
    }

    private var callbacks: Callbacks? = null

    // List view model defined by lazy
    private val addTaskViewModel: AddTaskViewModel by lazy {
        ViewModelProviders.of(this).get(AddTaskViewModel::class.java)
    }

    // widgets on screen
    private lateinit var prioritySelection : Spinner
    private lateinit var customBackButton : ImageView
    private lateinit var confirmNewTask : ImageView
    private lateinit var taskName: EditText
    private lateinit var courseName: EditText
    private lateinit var dueDateSelect : LinearLayout
    private lateinit var dueTimeSelect : LinearLayout
    private lateinit var personDateSelect : LinearLayout
    private lateinit var personTimeSelect : LinearLayout
    private lateinit var reminderSelect : LinearLayout


    // text views on screen
    private lateinit var addDueDate: TextView
    private lateinit var addDueTime: TextView
    private lateinit var addPersonalDate: TextView
    private lateinit var addPersonalTime: TextView
    private lateinit var addReminderText: TextView
    private lateinit var headerText : TextView

    // priority spinner
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
        dueDateSelect = view.findViewById(R.id.due_date_select) as LinearLayout
        dueTimeSelect = view.findViewById(R.id.due_time_select) as LinearLayout
        personDateSelect = view.findViewById(R.id.personal_date_select) as LinearLayout
        personTimeSelect = view.findViewById(R.id.personal_time_select) as LinearLayout
        reminderSelect = view.findViewById(R.id.alerts_date_select) as LinearLayout

        /* AGAIN NOT SURE ABOUT THESE */
        addDueDate = view.findViewById(R.id.add_due_date_text) as TextView
        addDueTime = view.findViewById(R.id.add_due_time_text) as TextView
        addPersonalDate = view.findViewById(R.id.add_personal_date_text) as TextView
        addPersonalTime = view.findViewById(R.id.add_personal_time_text) as TextView
        addReminderText = view.findViewById(R.id.add_reminder_text) as TextView
        headerText = view.findViewById(R.id.add_task_header) as TextView
        addPrioritySpinner = view.findViewById(R.id.spinner_priority) as Spinner

        Log.d(TAG, "New task ID: ${addTaskViewModel.newTask.taskID}")

        // setup spinner
        setupSpinnerAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //If the user wants to edit the task
        // Will populate the fragment with the task information
        if(arguments != null) {
            val passedTaskID: UUID = arguments?.getSerializable(ARG_EDIT_TASK_ID) as UUID
            Log.d(TAG, "args bundle task ID: $passedTaskID")
            addTaskViewModel.removeChecks() // remove any restraints on data
            addTaskViewModel.load(passedTaskID)
            //We want the header to read edit task instead of add task
            headerText.text = "Edit Task"

            addTaskViewModel.taskLiveData.observe(
                viewLifecycleOwner,
                Observer { task ->
                    task?.let {
                        //Grabs information from ViewModel
                        addTaskViewModel.newTask = task
                        //Updates UI every time change is observed
                        usePassedTaskInfo()
                    }
                })
        }
    }

    override fun onStart() {
        super.onStart()

        // Update text for task name
        taskName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                //DO NOTHING
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addTaskViewModel.newTask.taskName = sequence.toString()
                addTaskViewModel.taskNameCheck = true
            }
        })

        // Update text for course name
        courseName.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                //DO NOTHING
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //DO NOTHING
            }

            override fun onTextChanged(sequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                addTaskViewModel.newTask.courseName = sequence.toString()
            }
        })

        // Final Deadline Selection - Date
        dueDateSelect.setOnClickListener {
            // Log.d(TAG, "Select due date for deadline")
            DatePickerFragment.newInstance(addTaskViewModel.newTask.finalDeadlineDate, 1)
                .apply {
                    setTargetFragment(this@AddTaskFragment, REQUEST_DATE)
                    show(this@AddTaskFragment.requireFragmentManager(), DIALOG_DATE)
                }
        }

        // Final Deadline Selection - Time
        dueTimeSelect.setOnClickListener {
            // Log.d(TAG, "Select due time for deadline")

            if(addTaskViewModel.deadlineDateCheck) {
                TimePickerFragment.newInstance(addTaskViewModel.newTask.finalDeadlineTime, 1)
                    .apply {
                        setTargetFragment(this@AddTaskFragment, REQUEST_TIME)
                        show(this@AddTaskFragment.requireFragmentManager(), DIALOG_TIME)
                    }
            }
            //If user attempts to pick due date time before the due date itself
            else {
                Toast.makeText(activity,"Please select a due date FIRST", Toast.LENGTH_SHORT).show()
            }
        }

        // Personal Deadline Selection - Date
        personDateSelect.setOnClickListener {
            // Log.d(TAG, "Select due date for personal")
            if(addTaskViewModel.deadlineDateCheck) {
                DatePickerFragment.newInstance(addTaskViewModel.newTask.personalDeadlineDate, 2)
                    .apply {
                        setTargetFragment(this@AddTaskFragment, REQUEST_DATE)
                        show(this@AddTaskFragment.requireFragmentManager(), DIALOG_DATE)
                    }
            }
            else {
                Toast.makeText(activity,"Please select a due date FIRST", Toast.LENGTH_SHORT).show()
            }
        }

        // Personal Deadline Selection - Time
        personTimeSelect.setOnClickListener {
            // Log.d(TAG, "Select due time for personal")
            if(addTaskViewModel.personalDateCheck) {
                TimePickerFragment.newInstance(addTaskViewModel.newTask.personalDeadlineTime, 2)
                    .apply {
                        setTargetFragment(this@AddTaskFragment, REQUEST_TIME)
                        show(this@AddTaskFragment.requireFragmentManager(), DIALOG_TIME)
                    }
            }
            //If user attempts to add personal date before final due date
            else if(!addTaskViewModel.deadlineDateCheck) {
                Toast.makeText(activity,"Please select a due date FIRST", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(activity,"Please select a personal date FIRST", Toast.LENGTH_SHORT).show()
            }

        }

        // <-- button
        customBackButton.setOnClickListener {
            // Log.d(TAG, "Back button pressed!")
            callbacks?.onBackPressed()
        }

        // check mark button on top right
        confirmNewTask.setOnClickListener {
            Log.d(TAG, "New task being added!")
            // addTaskViewModel.newTask.taskName = "UI Mockup Design"
            // addTaskViewModel.newTask.courseName = "Mobile Computing"
            // addTaskViewModel.newTask.priority = "High Priority"

            // If task is being edited and check mark is clicked
            if(arguments != null) {
                //ViewModel -> Repository -> DAO
                addTaskViewModel.saveTask()
                callbacks?.onBackPressed()
            }
             else {
                //If adding task for the first time need to make sure the due date and task name are provided
                if(addTaskViewModel.deadlineDateCheck && addTaskViewModel.taskNameCheck) {
                    //ViewModel -> Repository -> DAO
                    addTaskViewModel.addTask()
                    callbacks?.onBackPressed()
                }
                //If task name hasnt been provided
                else if (!addTaskViewModel.taskNameCheck){
                    Toast.makeText(activity,"Please provide name to task", Toast.LENGTH_SHORT).show()
                }
                //If due date hasnt been provided
                else if (!addTaskViewModel.deadlineDateCheck){
                    Toast.makeText(activity,"Please select a due date", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Reminder Selection
        reminderSelect.setOnClickListener {
            Log.d(TAG, "Hit reminder button")
            if(addTaskViewModel.deadlineDateCheck) {
                DatePickerFragment.newInstance(addTaskViewModel.newTask.finalDeadlineDate, 3)
                    .apply {
                        setTargetFragment(this@AddTaskFragment, REQUEST_DATE)
                        show(this@AddTaskFragment.requireFragmentManager(), DIALOG_DATE)
                    }
            }
            else {
                Toast.makeText(activity,"Please select a due date", Toast.LENGTH_SHORT).show()
            }
        }


        // priority selection
        prioritySelection.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            //Used to determine what priority the user selects and updates the task info in ViewModel
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "Selected item from spinner position = $position")
                when (position) {
                    0 -> addTaskViewModel.newTask.priority = "Low Priority"
                    1 -> addTaskViewModel.newTask.priority = "Medium Priority"
                    2 -> addTaskViewModel.newTask.priority = "High Priority"
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }


    //Will handle formatting the date when selected inside fragment
    //Will update the information inside the ViewModel
    override fun onDateSelected(date: Date, protocol : Int) {
        Log.i(TAG, "Protocol: $protocol and Date: $date")
        when (protocol) {
            1 ->  { // Due date was selected for date input
                // Set dates
                addTaskViewModel.newTask.finalDeadlineDate = date
                addTaskViewModel.newTask.personalDeadlineDate = date

                // Format text
                val df = DateFormat.format("EEE MMM dd, yyyy", date)
                addDueDate.text = df

                // remove check -- Used to make sure if deadline has been first selected
                addTaskViewModel.deadlineDateCheck = true

                //Default time for the remaining fields
                // Assignments are usually due at 11:59 PM
                date.hours = 23
                date.minutes = 59
                date.seconds = 0

                // Clever trick to figure out if we have data entered for personal and reminder
                addTaskViewModel.newTask.finalDeadlineTime = date
                addTaskViewModel.newTask.personalDeadlineTime = date
                addTaskViewModel.newTask.reminderFrequency = date
                addTaskViewModel.newTask.reminderTime = date

                val df2 = DateFormat.format("hh:mm a", date)
                // Log.i(TAG, "df = $df")
                addDueTime.text = df2

                //When date is picked, personal and reminder will be cleared
                addPersonalDate.text = "---"
                addPersonalTime.text = "---"
                addReminderText.text = "---"

            }
            2 ->  { // when you select date for personal deadline
                addTaskViewModel.newTask.personalDeadlineDate = date
                addTaskViewModel.newTask.personalDeadlineTime = date
                val df = DateFormat.format("EEE MMM dd, yyyy", date)
                // Log.i(TAG, "df = $df")
                addPersonalDate.text = df

                // remove check that prevents from creating task
                addTaskViewModel.personalDateCheck = true
            }

            3 -> { // when you select a date for reminder
                addTaskViewModel.newTask.reminderFrequency = date
                addTaskViewModel.newTask.reminderTime = date

                // now ask for a time immediately
                TimePickerFragment.newInstance(addTaskViewModel.newTask.reminderFrequency, 3)
                    .apply {
                        setTargetFragment(this@AddTaskFragment, REQUEST_TIME)
                        show(this@AddTaskFragment.requireFragmentManager(), DIALOG_TIME)
                    }
            }
        }
    }

    //Will handle formatting the time when selected inside fragment
    //Will update the information inside the ViewModel
    override fun onTimeSelected(time: Date, protocol : Int) {
        Log.i(TAG, "Protocol: $protocol and Date: $time")
        when (protocol) {
            //When we click on the due date time
            1 ->  {
                // set view model and update text
                addTaskViewModel.newTask.finalDeadlineTime = time
                val df = DateFormat.format("hh:mm a", time)
                // Log.i(TAG, "df = $df")
                addDueTime.text = df

            }
            //When we click on the personal date time
            2 -> {
                // set view model and update text
                addTaskViewModel.newTask.personalDeadlineTime = time
                val df = DateFormat.format("hh:mm a", time)
                // Log.i(TAG, "df = $df")
                addPersonalTime.text = df
            }
            //When we click on the reminder time
            3 -> {
                // set view model and update text
                addTaskViewModel.newTask.reminderTime = time
                val df = DateFormat.format("EEE MMM dd, yyyy   -    hh:mm a", time)
                addReminderText.text = df

                // setup alarm
                callbacks?.onSetReminder(time, addTaskViewModel.newTask.taskName)
            }
        }
    }

    // Adapater is necessary to use spinner within layout
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

    //Grabbing information for the task from the ViewModel
    private fun usePassedTaskInfo() {
        // Basic Info
        taskName.setText(addTaskViewModel.newTask.taskName)
        courseName.setText(addTaskViewModel.newTask.courseName)

        // Date + Time
        val df1 = DateFormat.format("EEE MMM dd, yyyy", addTaskViewModel.newTask.finalDeadlineDate)
        val df2 = DateFormat.format("EEE MMM dd, yyyy", addTaskViewModel.newTask.personalDeadlineDate)
        val df3 = DateFormat.format("hh:mm a", addTaskViewModel.newTask.finalDeadlineTime)
        val df4 = DateFormat.format("hh:mm a", addTaskViewModel.newTask.personalDeadlineTime)
        // Log.i(TAG, "df = $df")

        addDueDate.text = df1
        addDueTime.text = df3

        if(df1 == df2 && df3 == df4) {
            addPersonalDate.text = "---"
            addPersonalTime.text = "---"
        }
        else {
            addPersonalDate.text = df2
            addPersonalTime.text = df4
        }

        if(addTaskViewModel.newTask.personalDeadlineDate != addTaskViewModel.newTask.reminderFrequency) {
            val df = DateFormat.format("EEE MMM dd, yyyy   -    hh:mm a", addTaskViewModel.newTask.reminderFrequency)
            addReminderText.text = df
        }

        when(addTaskViewModel.newTask.priority) {
            "Low Priority" -> addPrioritySpinner.setSelection(0)
            "Medium Priority" -> addPrioritySpinner.setSelection(1)
            "High Priority" -> addPrioritySpinner.setSelection(2)
        }
    }

    companion object {
        fun newInstance(): AddTaskFragment {
            return AddTaskFragment()
        }

        fun newInstance(taskID : UUID):AddTaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_EDIT_TASK_ID, taskID)
            }
            return AddTaskFragment().apply {
                arguments = args
            }
        }
    }
}