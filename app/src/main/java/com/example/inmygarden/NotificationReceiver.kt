package com.example.inmygarden

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationReceiver : BroadcastReceiver() {
    private val channelID = "1"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null) {
            val notificationIntent = Intent(context, LoginActivity::class.java)
            val notificationPendingIntent = PendingIntent.getActivities(context, 0,
                arrayOf(notificationIntent), PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE)
            val builder = NotificationCompat.Builder(context, channelID)
            val channel = NotificationChannel(channelID, "1",  NotificationManager.IMPORTANCE_HIGH)
            val manager : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

            builder.setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Title")
                .setContentText("Notification Text")
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true)

            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(1, builder.build())
        }
    }

}