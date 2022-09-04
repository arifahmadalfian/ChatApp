package com.example.chatapp.presentation.notification

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.di.FirstNotificationCompactBuilder
import com.example.chatapp.di.SecondNotificationCompactBuilder
import com.example.chatapp.di.ThirdNotificationCompactBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    @FirstNotificationCompactBuilder
    private val notificationBuilder: NotificationCompat.Builder,
    @SecondNotificationCompactBuilder
    private val notificationBuilder2: NotificationCompat.Builder,
    @ThirdNotificationCompactBuilder
    private val notificationBuilder3: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat
): ViewModel() {

    fun showSimpleNotification() {
        notificationManager.notify(1, notificationBuilder.build())
    }

    fun replyNotification() {
        notificationManager.notify(4, notificationBuilder3.build())
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

    fun showProgressNotification() {
        val max = 10
        var progress = 0
        viewModelScope.launch {
            while (progress != max) {
                delay(1000)
                progress += 1
                notificationManager.notify(
                    3,
                    notificationBuilder2
                        .setContentText("Downloading")
                        .setContentText("${progress}/${max}")
                        .setProgress(max, progress, false)
                        .build()
                )
            }
            notificationManager.notify(
                3,
                notificationBuilder
                    .setContentTitle("Completed")
                    .setContentText("")
                    .setContentIntent(null)
                    .clearActions()
                    .setProgress(0, 0, false)
                    .build()
            )
        }
    }
}