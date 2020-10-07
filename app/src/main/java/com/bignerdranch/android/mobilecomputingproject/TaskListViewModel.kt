package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository

class TaskListViewModel : ViewModel(){

    private val taskRepository = TaskRepository.get()
    var taskListLiveData = taskRepository.getIncompleteTasks()

    fun switchIncomplete() {
        taskListLiveData = taskRepository.getIncompleteTasks()
    }

    fun switchComplete() {
        taskListLiveData = taskRepository.getCompleteTasks()
    }

    fun switchDeadlineSort() {
        taskListLiveData = taskRepository.getDeadlineSorted()
    }

    fun switchPersonalSort() {
        taskListLiveData = taskRepository.getPersonalSorted()
    }

    // 420
    fun getHigh() {
        taskListLiveData = taskRepository.getHighPriority()
    }

    fun getCourseSort() {
        taskListLiveData = taskRepository.getCourseSort()
    }


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