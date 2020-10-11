package com.bignerdranch.android.mobilecomputingproject

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.*


private const val TAG = "MainActivity"
private const val RC_SIGN_IN = 0

class MainActivity : AppCompatActivity(),
    TaskListFragment.Callbacks, AddTaskFragment.Callbacks, TaskFragment.Callbacks {

    // Creating a Google Client
    private var mGoogleSignInClient: GoogleSignInClient? = null

    // Button for sign-in screen
    private lateinit var signInButton : com.google.android.gms.common.SignInButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        //creating the request to be signed in
        createRequest()

        signInButton = findViewById(R.id.sign_in_button)

        //call sign in when sign in button clicked
        signInButton.setOnClickListener {
            signIn()
        }

        setupNotification()

    }

    //Will create a request to pop up the google sign in and then grab the user client
    private fun createRequest() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    //Will be called inside the sign-in button listener
    //Will fire the signInIntent attempting to log in
    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask : Task<GoogleSignInAccount>){
        try {
            //If sign in was successful
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "Successfully logged in!")
            // Call our Fragment Code
            startFragmentBasedActivities()

            //If sign in failed
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
        }
    }


    // Fragment Code
    private fun startFragmentBasedActivities() {
        //Regular view start up without sign in
        setContentView(R.layout.activity_main)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

       if (currentFragment == null) {
           val fragment = TaskListFragment.newInstance()
           supportFragmentManager
               .beginTransaction()
               .add(R.id.fragment_container, fragment)
               .commit()
       }
        else {
           val fragment = TaskListFragment.newInstance(true)
           supportFragmentManager
               .beginTransaction()
               .replace(R.id.fragment_container, fragment)
               .commit()
       }
    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        //If user is already signed in previously it will skip the sign-in and go straight to TaskList
        if(account != null){
            Log.d(TAG, "We found a User in onStart()")
            startFragmentBasedActivities()
        }
        else {
            Log.d(TAG, "No User found in onStart()")
        }

    }


    //Creating TaskFragment with the passed in taskID
    override fun onTaskSelected(taskID: UUID) {
        Log.d(TAG, "MainActivity.onTaskSelected: $taskID")
        val fragment = TaskFragment.newInstance(taskID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    //Creating AddTaskFragment when user would like to create new task
    override fun onAddTask() {
        Log.d(TAG, "MainActivity.onAddTask()")
        val fragment = AddTaskFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    //Will just pop from the stack and go back to previous fragment
    override fun onBackPressed() {
        Log.d(TAG, "Back button pressed from Add Task Fragment!")
        super.onBackPressed()
    }

    //Will just pop from the stack and go back to previous fragment
    override fun onBackArrow() {
        Log.d(TAG, "Back button pressed from Task Fragment!")
        super.onBackPressed()
    }

    //Will launch AddTaskFragment with the TaskID when the user wants to edit a task
    override fun onEditSelected(taskId: UUID) {
        Log.d(TAG, "Edit button pressed from Task Fragment!")
        val fragment = AddTaskFragment.newInstance(taskId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    //User signs out
    override fun onSignOut() {
        // super.onBackPressed()
        // startFragmentBasedActivities()
        recreate()
    }

    // Alarm Code
    private fun setupNotification() {
        val taskRepository = TaskRepository.get()
        val liveData = taskRepository.getIncompleteTasks()

        liveData.observe(
            this,
            Observer { tasks ->
                tasks?.let {
                    Log.d(TAG, "We have ${tasks.size} incomplete tasks")
                    launchIntentNotification(tasks.size)
                }
            })
    }

    private fun launchIntentNotification(taskNum : Int) {

        // setting up the alarm
        val alertTime = Calendar.getInstance()
        alertTime.set(Calendar.HOUR_OF_DAY, 9)
        alertTime.set(Calendar.MINUTE, 0)
        alertTime.set(Calendar.SECOND, 0)


        // starting the alarm
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        intent.putExtra("TASKS", taskNum)
        intent.action = "DailyAlarm"
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
         if (alertTime.before(Calendar.getInstance())) {
            alertTime.add(Calendar.DATE, 1)
         }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alertTime.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)

        Log.d(TAG, "Setup daily alarm")
    }

    override fun onSetReminder(reminderTime : Date, taskName : String) {
        // setting up the alarm
        val alertTime = Calendar.getInstance()
        alertTime.time = reminderTime

        // starting the alarm
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlertReceiver::class.java)
        intent.putExtra("NAME", taskName)
        intent.action = "ReminderAlarm"
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime.timeInMillis, pendingIntent)

        Log.d(TAG, "We set a reminder for $taskName on $reminderTime")
    }
}