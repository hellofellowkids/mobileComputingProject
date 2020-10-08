package com.bignerdranch.android.mobilecomputingproject

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.math.sign

private const val TAG = "MainActivity"
private const val RC_SIGN_IN = 0

class MainActivity : AppCompatActivity(),
    TaskListFragment.Callbacks, AddTaskFragment.Callbacks, TaskFragment.Callbacks {
    val context=this

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var signInButton : Button
    private var mAuth: FirebaseAuth? = null

    // Firebase functions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        mAuth = FirebaseAuth.getInstance()

        createRequest()

        signInButton = findViewById(R.id.sign_in_button)

        signInButton.setOnClickListener {
            signIn()
        }

        /*
        // Fragment Code

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = TaskListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
        */
    }

    private fun createRequest() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
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
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = mAuth?.currentUser

                    // Now initialize starting fragment
                    startFragmentBasedActivities()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }

                // ...
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

        val user : FirebaseUser? = mAuth?.currentUser

        // If the sign-in was successful...
        if(user != null){
            Log.d(TAG, "User is not null!")
            startFragmentBasedActivities()
        }
        Log.d(TAG, "User is null in onStart()!")
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