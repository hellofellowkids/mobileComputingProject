package com.bignerdranch.android.mobilecomputingproject.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.mobilecomputingproject.Task
import java.util.*

@Dao
interface TaskDao {

    //grabbing all incomplete tasks from database
    @Query("SELECT * FROM task WHERE complete = 0")
    fun getIncompleteTasks(): LiveData<List<Task>>

    //grabbing all complete tasks from database
    @Query("SELECT * FROM task WHERE complete = 1")
    fun getCompleteTasks(): LiveData<List<Task>>

    //grabbing all incomplete tasks sorted by due date
    @Query("SELECT * FROM task WHERE complete = 0 ORDER BY finalDeadlineDate")
    fun getDeadlineSorted(): LiveData<List<Task>>

    //grabbing all incomplete tasks sorted by personal date
    @Query("SELECT * FROM task WHERE complete = 0 ORDER BY personalDeadlineDate ")
    fun getPersonalSorted(): LiveData<List<Task>>

    //grabbing all incomplete tasks that are of high priority
    @Query("SELECT * FROM task WHERE complete = 0 AND priority = 'High Priority'")
    fun getHighPriority(): LiveData<List<Task>>

    //grabbing all incomplete tasks that are of medium priority
    @Query("SELECT * FROM task WHERE complete = 0 AND priority = 'Medium Priority'")
    fun getMedPriority(): LiveData<List<Task>>

    //grabbing all incomplete tasks that are of low priority
    @Query("SELECT * FROM task WHERE complete = 0 AND priority = 'Low Priority'")
    fun getLowPriority(): LiveData<List<Task>>

    //grabbing all incomplete tasks sorted by course name (alphabetical order)
    @Query("SELECT * FROM task WHERE complete = 0 ORDER BY courseName")
    fun getCourseSort(): LiveData<List<Task>>

    //grabbing all task with certain taskID
    @Query("SELECT * FROM task WHERE taskID=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    //updating task
    @Update
    fun updateTask(task: Task)

    //adding task
    @Insert
    fun addTask(task: Task)

    //deleting task with the given taskID
    @Query("DELETE FROM task WHERE taskID=(:id)")
    fun deleteTask(id: UUID)
}