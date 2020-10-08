package com.bignerdranch.android.mobilecomputingproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    val context=this

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var signInButton : Button

    // Firebase functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        createRequest()

        signInButton = findViewById(R.id.sign_in_button)

        signInButton.setOnClickListener {
            signIn()
        }

    }

    private fun createRequest() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

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
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "Successfully logged in!")
            // Call our Fragment Code
            startFragmentBasedActivities()

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
        }
    }


    // Fragment Code
    private fun startFragmentBasedActivities() {
        setContentView(R.layout.activity_main)
       val currentFragment =
           supportFragmentManager.findFragmentById(R.id.fragment_container)

       if (currentFragment == null) {
           val fragment = TaskListFragment.newInstance()
           supportFragmentManager
               .beginTransaction()
               .add(R.id.fragment_container, fragment)
               .commit()
       }

    }


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)

        if(account != null){
            Log.d(TAG, "We found a User in onStart()")
            startFragmentBasedActivities()
        }

        Log.d(TAG, "No User found in onStart()")

    }


    override fun onTaskSelected(taskID: UUID) {
        Log.d(TAG, "MainActivity.onTaskSelected: $taskID")
        val fragment = TaskFragment.newInstance(taskID)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onAddTask() {
        Log.d(TAG, "MainActivity.onAddTask()")
        val fragment = AddTaskFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        Log.d(TAG, "Back button pressed from Add Task Fragment!")
        super.onBackPressed()
    }

    override fun onBackArrow() {
        Log.d(TAG, "Back button pressed from Task Fragment!")
        super.onBackPressed()
    }

    override fun onEditSelected(taskId: UUID) {
        Log.d(TAG, "Edit button pressed from Task Fragment!")
        val fragment = AddTaskFragment.newInstance(taskId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}