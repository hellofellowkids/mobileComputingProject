package com.bignerdranch.android.mobilecomputingproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


private const val TAG = "AlertReceiver"

class AlertReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d(TAG, "Received Broadcast")

        Log.d(TAG, "Intent Action : ${intent.action}")

        //If the receiver receives the DailyAlarm Intent
        if (intent.action.equals("DailyAlarm")) {

            //Grab the number of incomplete tasks from the intent extra
            val taskNum = intent.getIntExtra("TASKS", -999)
            Log.d(TAG, "taskNum = $taskNum")

            //If user has any incomplete tasks they will be notified so
            if (taskNum > 0) {
                val resources = context.resources
                val notification = NotificationCompat
                    .Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.daily_reminder_title))
                    .setContentText("You got $taskNum incomplete tasks left. Get on it!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(0, notification)
                //If the user has no incomplete tasks they will be notified so
            } else {
                val resources = context.resources
                val notification = NotificationCompat
                    .Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.daily_reminder_title))
                    .setContentText("Congrats! You have completed all your assignments")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                val notificationManager = NotificationManagerCompat.from(context)
                notificationManager.notify(0, notification)
            }
        }

        //If the receiver receives the ReminderAlarm that the user has set for individual tasks
        else if (intent.action.equals("ReminderAlarm")) {
            val taskName = intent.getStringExtra("NAME")
            Log.d(TAG, "taskName = $taskName")

            //Will grab taskName from the intent extra and display following notification
            val resources = context.resources
            val notification = NotificationCompat
                .Builder(context, NOTIFICATION_CHANNEL2_ID)
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(resources.getString(R.string.daily_reminder_title))
                .setContentText("Friendly reminder for your $taskName task. Get on it!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(0, notification)
        }
    }
}