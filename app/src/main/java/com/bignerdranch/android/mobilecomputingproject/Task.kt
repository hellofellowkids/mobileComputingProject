package com.bignerdranch.android.mobilecomputingproject

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task (
    @PrimaryKey val taskID : UUID = UUID.randomUUID(),
    var taskName : String = "",
    var courseName : String = "",
    var priority : String? = "",
    var finalDeadlineDate : Date = Date(),
    var finalDeadlineTime : Date = Date(),
    var personalDeadlineDate : Date = Date(),
    var personalDeadlineTime : Date = Date(),
    var reminderFrequency : Date = Date(),
    var complete : Boolean = false
)