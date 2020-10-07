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

    @Query("SELECT * FROM task WHERE complete = 0 ORDER BY finalDeadlineDate")
    fun getDeadlineSorted(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = 0 ORDER BY personalDeadlineDate ")
    fun getPersonalSorted(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = 0 AND priority = 'High Priority'")
    fun getHighPriority(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = 0 AND priority = 'Medium Priority'")
    fun getMedPriority(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = 0 AND priority = 'Low Priority'")
    fun getLowPriority(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE complete = 0 ORDER BY courseName")
    fun getCourseSort(): LiveData<List<Task>>

    @Query("SELECT * FROM task WHERE taskID=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Query("DELETE FROM task WHERE taskID=(:id)")
    fun deleteTask(id: UUID)
}