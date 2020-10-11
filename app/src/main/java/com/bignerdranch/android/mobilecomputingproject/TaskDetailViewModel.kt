package com.bignerdranch.android.mobilecomputingproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository
import java.io.File
import java.util.*

class TaskDetailViewModel() : ViewModel() {

    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()

    var taskLiveData: LiveData<Task?> =
        Transformations.switchMap(taskIdLiveData) { taskID ->
            taskRepository.getTask(taskID)
        }

    fun load(taskID: UUID) {
        taskIdLiveData.value = taskID
    }

    //Will call taskRepository updateTask
    fun saveTask(task : Task) {
        taskRepository.updateTask(task)
    }

    //Will call taskRepository deleteTask
    fun deleteTask(task : Task) {
        taskRepository.deleteTask(task)
    }

    //Will call taskRepository getPhotoFile
    fun getPhotoFile(task: Task) : File {
        return taskRepository.getPhotoFile(task)
    }
}