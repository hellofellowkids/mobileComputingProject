package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository

class TaskListViewModel : ViewModel(){

    private val taskRepository = TaskRepository.get()
    val taskListLiveData = taskRepository.getTasks()

    /*
    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }
     */

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