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

    // checks before user can create a task
    var deadlineDateCheck : Boolean = false
    var personalDateCheck : Boolean = false
    var taskNameCheck : Boolean = false

    // easy way for if we editing a task to remove those checks
    fun removeChecks() {
        //checking conditions inside AddTaskFragment
        deadlineDateCheck = true
        taskNameCheck = true
        personalDateCheck = true
    }

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

    //load task by ID
    fun load(taskID: UUID) {
        taskIdLiveData.value = taskID
    }

    //save task to database when editing task information
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