package com.bignerdranch.android.mobilecomputingproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.bignerdranch.android.mobilecomputingproject.database.TaskRepository

const val NOTIFICATION_CHANNEL_ID = "daily_reminder"
const val NOTIFICATION_CHANNEL2_ID = "task_reminder"

class STMApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // First Channel is for Daily Reminders
            val name = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
            val notificationManager: NotificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            // Second Channel is for Task Reminders
            val name2 = getString(R.string.notification_channel_name2)
            val channel2 =
                NotificationChannel(NOTIFICATION_CHANNEL2_ID, name2, importance)
            notificationManager.createNotificationChannel(channel2)

        }
    }
}