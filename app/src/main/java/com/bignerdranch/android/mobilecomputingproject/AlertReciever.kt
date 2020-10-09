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

        val taskNum = intent.extras?.getInt("TASKS")

        val resources = context.resources
        val notification = NotificationCompat
            .Builder(context, NOTIFICATION_CHANNEL_ID)
            .setTicker(resources.getString(R.string.daily_reminder_title))
            .setSmallIcon(android.R.drawable.ic_menu_report_image)
            .setContentTitle(resources.getString(R.string.daily_reminder_title))
            .setContentText("Hey! Come back and take a look at your incomplete tasks.")
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, notification)
    }
}