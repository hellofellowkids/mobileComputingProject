package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private const val TAG = "TaskListFragment"

class TaskListFragment : Fragment() {

    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onTaskSelected(taskID: UUID)
        fun onAddTask()
    }

    private var callbacks: Callbacks? = null

    // widgets inside task list
    private lateinit var taskRecycleView : RecyclerView
    private lateinit var addTaskButton : FloatingActionButton
    private var adapter: TaskAdapter? = null

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total tasks: ${taskListViewModel.tasks.size}")
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
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        // find widgets in layout
        taskRecycleView = view.findViewById(R.id.task_recycler_view) as RecyclerView
        addTaskButton = view.findViewById(R.id.add_task_button) as FloatingActionButton

        updateUI()

        return view
    }

    override fun onStart() {
        super.onStart()

        // + button
        addTaskButton.setOnClickListener{
            // Toast.makeText(context, "Add Button pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onAddTask()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI() {
        val tasks = taskListViewModel.tasks
        adapter = TaskAdapter(tasks)
        taskRecycleView.adapter = adapter
    }

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
            courseName.text = this.task.taskName
            taskPriority.text = this.task.taskName
            finalDeadline.text = this.task.taskName
            personalDeadline.text = this.task.taskName
        }

        override fun onClick(v: View) {
            // Toast.makeText(context, "${task.taskName} pressed!", Toast.LENGTH_SHORT).show()
            callbacks?.onTaskSelected(task.taskID)
        }

    }

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