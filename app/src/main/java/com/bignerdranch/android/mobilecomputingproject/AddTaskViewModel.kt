package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository
import java.util.*

class AddTaskViewModel : ViewModel(){

    // new task information
    private val taskRepository = TaskRepository.get()
    var newTask = Task()

    // insert task into database
    fun addTask() {
        taskRepository.addTask(newTask)
    }

    // passed task information
    private val taskIdLiveData = MutableLiveData<UUID>()
    var taskLiveData: LiveData<Task?> =
        Transformations.switchMap(taskIdLiveData) { taskID ->
            taskRepository.getTask(taskID)
        }

    fun load(taskID: UUID) {
        taskIdLiveData.value = taskID
    }

    fun saveTask() {
        taskRepository.updateTask(newTask)
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