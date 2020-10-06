package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "TaskListFragment"

class TaskListFragment : Fragment() {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onTaskSelected(taskID: UUID)
        fun onAddTask()
    }

    private var callbacks: Callbacks? = null

    // related to Recycle View
    private lateinit var taskRecycleView : RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    // List view model defined by lazy
    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    // widgets outside of recycle view
    private lateinit var addTaskButton : FloatingActionButton
    private lateinit var taskFilter : Spinner


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
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        // find widgets in layout
        taskRecycleView = view.findViewById(R.id.task_recycler_view) as RecyclerView
        addTaskButton = view.findViewById(R.id.add_task_button) as FloatingActionButton
        taskFilter = view.findViewById(R.id.spinner_task_filter) as Spinner

        // setup recycle view
        taskRecycleView.layoutManager = LinearLayoutManager(context)
        taskRecycleView.adapter = adapter

        // setup spinner
        setupSpinnerAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskListLiveData.observe(
            viewLifecycleOwner,
            Observer { tasks ->
                tasks?.let {
                    Log.i(TAG, "Got ${tasks.size} tasks")
                    updateUI(tasks)
                }
            })
    }

    override fun onStart() {
        super.onStart()

        // + button
        addTaskButton.setOnClickListener{
            // Toast.makeText(context, "Add Button pressed!", Toast.LENGTH_SHORT).show()
            // val task = Task()
            // taskListViewModel.addTask(task)
            callbacks?.onAddTask()
            // callbacks?.onAddTask()
        }

        // task filter
        taskFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    private fun updateUI(tasks: List<Task>) {
        adapter = TaskAdapter(tasks)
        taskRecycleView.adapter = adapter
    }

    // setup spinner
    private fun setupSpinnerAdapter() {
        // Array adapter for task filter
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.sort_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                taskFilter.adapter = adapter
            }
        }
    }

    // Item holders for recycle view
    private inner class TaskHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        private lateinit var task: Task

        // Pass list_item_task widgets
        val taskName: TextView = itemView.findViewById(R.id.task_name)
        val courseName: TextView = itemView.findViewById(R.id.course_name)
        val taskPriority: TextView = itemView.findViewById(R.id.task_priority)
        val finalDeadline: TextView = itemView.findViewById(R.id.final_deadline)
        val personalDeadline: TextView = itemView.findViewById(R.id.personal_deadline)

        fun bind(task: Task) {
            this.task = task
            taskName.text = this.task.taskName
            courseName.text = this.task.courseName
            taskPriority.text = this.task.priority
            finalDeadline.text = this.task.finalDeadlineDate.toString()
            personalDeadline.text = this.task.personalDeadlineDate.toString()
        }

        override fun onClick(v: View) {
            // Toast.makeText(context, "${task.taskName} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onTaskSelected(task.taskID)
        }

    }

    // Adapter for recycler view
    private inner class TaskAdapter(var tasks: List<Task>)
        : RecyclerView.Adapter<TaskHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : TaskHolder {
            // Inflate list_item_task view
            val view = layoutInflater.inflate(R.layout.list_item_task, parent, false)
            return TaskHolder(view)
        }

        override fun getItemCount() = tasks.size

        override fun onBindViewHolder(holder: TaskHolder, position: Int) {
            val task = tasks[position]
            holder.apply {
                // bind task to view item
                holder.bind(task)
            }
        }
    }

    companion object {
        fun newInstance(): TaskListFragment {
            return TaskListFragment()
        }
    }
}
