package com.bignerdranch.android.mobilecomputingproject

import java.util.*

data class Task (
    val taskID : UUID = UUID.randomUUID(),
    val taskName : String
)