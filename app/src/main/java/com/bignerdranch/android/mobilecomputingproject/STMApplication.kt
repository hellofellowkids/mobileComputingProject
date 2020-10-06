package com.bignerdranch.android.mobilecomputingproject

import android.app.Application
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository

class STMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}