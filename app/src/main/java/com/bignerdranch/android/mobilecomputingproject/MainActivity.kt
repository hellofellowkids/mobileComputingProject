package com.bignerdranch.android.mobilecomputingproject

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), TaskListFragment.Callbacks {
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

    // Adi's initial comment

    // Adi's 2nd comment

    // Stefano's comment

    // Vinit's comment





}