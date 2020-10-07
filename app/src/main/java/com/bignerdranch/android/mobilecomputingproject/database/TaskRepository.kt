package com.bignerdranch.android.mobilecomputingproject.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.bignerdranch.android.mobilecomputingproject.Task
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context) {

    private val database : TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getIncompleteTasks(): LiveData<List<Task>> = taskDao.getIncompleteTasks()

    fun getCompleteTasks(): LiveData<List<Task>> = taskDao.getCompleteTasks()

    fun getPersonalSorted(): LiveData<List<Task>> = taskDao.getPersonalSorted()

    fun getDeadlineSorted(): LiveData<List<Task>> = taskDao.getDeadlineSorted()

    fun getHighPriority(): LiveData<List<Task>> = taskDao.getHighPriority()

    fun getMedPriority(): LiveData<List<Task>> = taskDao.getMedPriority()

    fun getLowPriority(): LiveData<List<Task>> = taskDao.getLowPriority()


    fun getCourseSort(): LiveData<List<Task>> = taskDao.getCourseSort()

    fun getTask(id: UUID): LiveData<Task?> = taskDao.getTask(id)

    fun updateTask(task: Task) {
        executor.execute {
            taskDao.updateTask(task)
        }
    }

    fun addTask(task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }

    fun deleteTask(task: Task) {
        executor.execute {
            taskDao.deleteTask(task.taskID)
        }
    }

    companion object {
        private var INSTANCE: TaskRepository? = null


        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get(): TaskRepository {
            return INSTANCE ?:
            throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}