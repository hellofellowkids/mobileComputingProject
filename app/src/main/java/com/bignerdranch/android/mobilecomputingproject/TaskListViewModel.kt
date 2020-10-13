package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.ViewModel
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository
import java.io.File

class TaskListViewModel : ViewModel(){

    private val taskRepository = TaskRepository.get()
    var taskListLiveData = taskRepository.getIncompleteTasks()

    // Will call all these functions from the Repository to help support task filter

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

    fun getHigh() {
        taskListLiveData = taskRepository.getHighPriority()
    }

    fun getMed(){
        taskListLiveData = taskRepository.getMedPriority()
    }

    fun getLow(){
        taskListLiveData = taskRepository.getLowPriority()
    }

    fun getCourseSort() {
        taskListLiveData = taskRepository.getCourseSort()
    }

    fun getPhotoFile(task: Task) : File {
        return taskRepository.getPhotoFile(task)
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