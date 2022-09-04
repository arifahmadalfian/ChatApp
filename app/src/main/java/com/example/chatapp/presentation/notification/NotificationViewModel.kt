package com.example.chatapp.presentation.notification

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.chatapp.receiver.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
): ViewModel() {

    fun showSimpleNotification() {
        notificationManager.notify(1, notificationBuilder.build())
    }

    fun updateSimpleNotification() {
        notificationManager.notify(
            2,
            notificationBuilder
                .setContentTitle("New Title")
                .setContentText("New Content Text")
                .build()
        )
    }

    fun cancelSimpleNotification() {
        notificationManager.cancel(1)
    }

    fun detailSimpleNotification() {
        notificationManager.notify(
            3,
            notificationBuilder
                .setContentTitle("Notification")
                .setContentText("Notification go to detail")
                .build()
        )
    }
}