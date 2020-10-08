package com.bignerdranch.android.mobilecomputingproject

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import org.w3c.dom.Text
import java.io.File
import java.util.*

private const val TAG = "TaskFragment"
private const val ARG_TASK_ID = "task_id"
private const val DIALOG_ALERT = "DialogAlert"
private const val REQUEST_ANSWER = 0
private const val REQUEST_PHOTO = 1

class TaskFragment : Fragment(), AlertDialogFragment.Callbacks {

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
    private lateinit var photoFile : File
    private lateinit var photoUri: Uri

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
                    photoFile = taskDetailViewModel.getPhotoFile(task)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.bignerdranch.android.mobileComputingProject.fileprovider",
                        photoFile)
                    updateUI()
                }
            })
    }

    private fun updateUI() {
        // set widget with info from 'task' var

        val df1 = DateFormat.format("EEE MMM dd, yyyy", task.finalDeadlineDate)
        val df2 = DateFormat.format("hh:mm a", task.finalDeadlineTime)
        val df3 = DateFormat.format("EEE MMM dd, yyyy", task.personalDeadlineDate)
        val df4 = DateFormat.format("hh:mm a", task.personalDeadlineTime)

        // Basic Info
        taskName.text = task.taskName
        priority.text = task.priority
        subjectName.text = task.courseName

        // Set Priority Color
        when(task.priority) {
            "Low Priority" -> priority.setTextColor(Color.parseColor("#009933"))
            "Medium Priority" -> priority.setTextColor(Color.parseColor("#C6B373"))
            "High Priority" -> priority.setTextColor(Color.parseColor("#ff0000"))
        }


        // Date + Time Info
        dueDate.text = df1.toString()
        dueTime.text = df2.toString()

        if(df1 == df3 && df2 == df4) {
            personalDate.text = "---"
            personalTime.text = "---"
        }
        else {
            personalDate.text = df3.toString()
            personalTime.text = df4.toString()
        }

        reminder.text =  "---" // task.reminderFrequency.toString() //This is the date when the notification will go out

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

        // gear icons
        editSettingsButton.setOnClickListener {
            //We want to edit this task
            Log.d(TAG, "Edit Settings Button Clicked")
            callbacks?.onEditSelected(task.taskID)
        }

        checkButton.setOnClickListener {
            //We want to complete this task
            Log.d(TAG, "Complete Button Clicked")
            task.complete = true
            taskDetailViewModel.saveTask(task)

            // Now prompt for camera
            AlertDialogFragment.newInstance().apply {
                setTargetFragment(this@TaskFragment, REQUEST_ANSWER)
                show(this@TaskFragment.requireFragmentManager(), DIALOG_ALERT)
            }

        }
    }

    override fun onStop() {
        super.onStop()
        // taskDetailViewModel.saveTask // save info of modified task
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_PHOTO) {
            requireActivity().revokeUriPermission(photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            // Toast.makeText(activity, "Photo taken successfully! Head back to your list", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPositiveClick() {
        Log.d(TAG, "Yes response!")

        // camera stuff
        val packageManager: PackageManager = requireActivity().packageManager

        val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(captureImage,
                PackageManager.MATCH_DEFAULT_ONLY)
        if (resolvedActivity == null) {
            Toast.makeText(activity,"This device does not have a camera", Toast.LENGTH_SHORT).show()
            callbacks?.onBackArrow()
        }

        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val cameraActivities: List<ResolveInfo> =
            packageManager.queryIntentActivities(captureImage,
                PackageManager.MATCH_DEFAULT_ONLY)

        for (cameraActivity in cameraActivities) {
            requireActivity().grantUriPermission(
                cameraActivity.activityInfo.packageName,
                photoUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        startActivityForResult(captureImage, REQUEST_PHOTO)

        // callbacks?.onBackArrow()
    }

    override fun onNegativeClick() {
        Log.d(TAG, "NO response!")
        callbacks?.onBackArrow()
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