package com.bignerdranch.android.mobilecomputingproject.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.mobilecomputingproject.Task
import java.util.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE complete = 0")
    fun getIncompleteTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = 1")
    fun getCompleteTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE taskID=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Query("DELETE FROM task WHERE taskID=(:id)")
    fun deleteTask(id: UUID)
}