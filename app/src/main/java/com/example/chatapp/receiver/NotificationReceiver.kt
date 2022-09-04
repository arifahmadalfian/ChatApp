package com.example.chatapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.app.RemoteInput
import com.example.chatapp.di.RESULT_KEY
import com.example.chatapp.di.SecondNotificationCompactBuilder
import com.example.chatapp.di.ThirdNotificationCompactBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver: BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManagerCompat
    @Inject
    @SecondNotificationCompactBuilder
    lateinit var notificationBuilderSecond: NotificationCompat.Builder
    @Inject
    @ThirdNotificationCompactBuilder
    lateinit var notificationBuilderThird: NotificationCompat.Builder

    override fun onReceive(context: Context?, intent: Intent?) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        if (remoteInput != null) {
            val input = remoteInput.getCharSequence(RESULT_KEY).toString()
            val person = Person.Builder().setName("Me").build()
            val message = NotificationCompat.MessagingStyle.Message(
                input, System.currentTimeMillis(), person
            )
            val notificationStyle = NotificationCompat.MessagingStyle(person).addMessage(message)
            notificationManager.notify(
                4,
                notificationBuilderThird
                    .setStyle(notificationStyle)
                    .build()
            )
        }

    }
}