package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel(){

    val tasks = mutableListOf<Task>()

    init {
        for (i in 1 until 100) {
            val task = Task(taskName = "Homework #$i")
            tasks += task
        }
    }
}