package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.graphics.Color
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
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter
import java.util.*

private const val TAG = "TaskListFragment"
private const val ARG_SIGN_IN = "sign-in-boolean"

class TaskListFragment : Fragment() {

    // Callbacks for fragment navigation
    interface Callbacks {
        fun onTaskSelected(taskID: UUID)
        fun onAddTask()
        fun onSignOut()
    }

    private var callbacks: Callbacks? = null

    // related to Recycle View
    private lateinit var taskRecycleView : RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    // getter methods for testing
    fun provideRecyclerView() : RecyclerView {
        return taskRecycleView
    }

    // List view model defined by lazy
    public val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    // widgets outside of recycle view
    private lateinit var addTaskButton : FloatingActionButton
    private lateinit var taskFilter : Spinner
    private lateinit var taskPrompt : TextView

    // network code
    private lateinit var signOutButton : Button
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private var signInCheck : Boolean = false

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
        taskPrompt = view.findViewById(R.id.no_message_prompt) as TextView

        // setup recycle view
        taskRecycleView.layoutManager = LinearLayoutManager(context)
        taskRecycleView.adapter = adapter

        // setup spinner
        setupSpinnerAdapter()

        // network code to pull Google account info
        signOutButton = view.findViewById(R.id.sign_out_button) as Button
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = activity?.let { GoogleSignIn.getClient(it, gso) }!!

        val account = GoogleSignIn.getLastSignedInAccount(activity)

        // If there was an account, grab pull their name and welcome them with toast
        if (account != null && !signInCheck && arguments == null) {
            val personName = "Welcome " + account.displayName
            Toast.makeText(activity, personName, Toast.LENGTH_SHORT).show()
            signInCheck = true // ensure welcome doesn't come every time you go to this fragment
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskListLiveData.observe(
            viewLifecycleOwner,
            Observer { tasks ->
                tasks?.let {
                    //grabbing the tasks and updating the UI
                    Log.i(TAG, "Got ${tasks.size} tasks")
                    updateUI(tasks)
                }
            })
    }

    override fun onStart() {
        super.onStart()

        // network code - sign out button
        signOutButton.setOnClickListener {
            //When user clicks the sign out button 
            mGoogleSignInClient.signOut().addOnCompleteListener {
                Toast.makeText(activity, "Successfully signed out!", Toast.LENGTH_SHORT).show()
                callbacks?.onSignOut()
            }
        }

        // + button
        addTaskButton.setOnClickListener{
            // Toast.makeText(context, "Add Button pressed!", Toast.LENGTH_SHORT).show()
            // val task = Task()
            // taskListViewModel.addTask(task)
            callbacks?.onAddTask()
            // callbacks?.onAddTask()
        }

        // task filter spinner
        taskFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d(TAG, "Selected item from spinner position = $position")
                //This will filter tasks according to spinner selection
                when(position) {
                    0 -> taskListViewModel.switchIncomplete()
                    1 -> taskListViewModel.switchComplete()
                    2 -> taskListViewModel.switchDeadlineSort()
                    3 -> taskListViewModel.switchPersonalSort()
                    4 -> taskListViewModel.getHigh()
                    5 -> taskListViewModel.getMed()
                    6 -> taskListViewModel.getLow()
                    7 -> taskListViewModel.getCourseSort()
                }

                taskListViewModel.taskListLiveData.observe(
                    viewLifecycleOwner,
                    Observer { tasks ->
                        tasks?.let {
                            Log.i(TAG, "Got ${tasks.size} tasks")
                            updatePrompt(tasks.size, position)
                            updateUI(tasks)
                        }
                    }) // end of observer
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

    private fun updatePrompt(taskSize : Int, position : Int){
        if(taskSize == 0) {
            //Will update prompts that appear on screen if there are no tasks for that selection
            when(position) {
                1 -> taskPrompt.text = "No completed tasks. You should get to work!"
                4 -> taskPrompt.text = "No incomplete tasks with high priority."
                5 -> taskPrompt.text = "No incomplete tasks with medium priority."
                6 -> taskPrompt.text = "No incomplete tasks with low priority."
                else -> {
                    taskPrompt.text = "No incomplete tasks. Press the '+' to add one"
                }
            }

        }
        else {
            taskPrompt.text = ""
        }

    }

    // setup spinner
    private fun setupSpinnerAdapter() {
        // Array adapter for task filter
        activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.sort_array,
                R.layout.spinner_text2
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown2)
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
        val assignPic : ImageView = itemView.findViewById(R.id.completed_task_photo)

        fun bind(task: Task) {
            this.task = task

            // basic task information
            taskName.text = this.task.taskName
            courseName.text = this.task.courseName
            taskPriority.text = this.task.priority

            if(task.courseName == "") {
                courseName.text = "No Course"
                courseName.setTextColor(Color.parseColor("#C0C0C0"))
            }

            // date + time information
            val df1 = DateFormat.format("EEE MMM dd, yyyy", task.finalDeadlineDate)
            val df2 = DateFormat.format("hh:mm a", task.finalDeadlineTime)
            val df3 = DateFormat.format("EEE MMM dd, yyyy", task.personalDeadlineDate)
            val df4 = DateFormat.format("hh:mm a", task.personalDeadlineTime)

            finalDeadline.text = "Due Date: " + df1.toString() + " " + df2.toString()

            if(task.personalDeadlineDate == task.finalDeadlineDate &&
                    task.personalDeadlineTime == task.finalDeadlineTime) {
                personalDeadline.text = "Personal: ---"
                personalDeadline.setTextColor(Color.parseColor("#C0C0C0"))
            }
            else {
                personalDeadline.text = "Personal: " + df3.toString() + " " + df4.toString()
            }

            when(task.priority) {
                "Low Priority" -> taskPriority.setTextColor(Color.parseColor("#009933"))
                "Medium Priority" -> taskPriority.setTextColor(Color.parseColor("#C6B373"))
                "High Priority" -> taskPriority.setTextColor(Color.parseColor("#ff0000"))
            }

            // Related to pictures
            val photoFile = taskListViewModel.getPhotoFile(task)
            if (photoFile.exists()) {
                val bitmap = getScaledBitmap(photoFile.path, requireActivity())
                assignPic.setImageBitmap(bitmap)
            } else {
                assignPic.setImageDrawable(null)
            }
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
        fun newInstance(alreadySignIn : Boolean): TaskListFragment {
            val args = Bundle().apply {
                putBoolean(ARG_SIGN_IN, alreadySignIn)
            }
            return TaskListFragment().apply {
                arguments = args
            }
        }
    }
}
