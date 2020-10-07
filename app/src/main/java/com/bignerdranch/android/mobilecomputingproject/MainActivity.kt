package com.bignerdranch.android.mobilecomputingproject

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    TaskListFragment.Callbacks, AddTaskFragment.Callbacks, TaskFragment.Callbacks {
    val context=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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