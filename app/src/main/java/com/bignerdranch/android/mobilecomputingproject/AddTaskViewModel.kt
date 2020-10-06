package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository

class AddTaskViewModel : ViewModel(){

    private val taskRepository = TaskRepository.get()
    val newTask = Task()

    fun addTask() {
        taskRepository.addTask(newTask)
    }

    /*
    init {
    val tasks = mutableListOf<Task>()
        for (i in 1 until 100) {
            val task = Task()
            tasks += task
        }
    }
    */
}