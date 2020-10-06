package com.bignerdranch.android.mobilecomputingproject

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task (
    @PrimaryKey val taskID : UUID = UUID.randomUUID(),
    var taskName : String = "",
    val courseName : String = "",
    val priority : String? = "",
    val finalDeadlineDate : Date = Date(),
    val finalDeadlineTime : Date = Date(),
    val personalDeadlineDate : Date = Date(),
    val personalDeadlineTime : Date = Date(),
    val reminderFrequency : Date = Date(),
    val complete : Boolean = false
)