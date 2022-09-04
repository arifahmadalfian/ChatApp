package com.example.chatapp.presentation.notification

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
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
}