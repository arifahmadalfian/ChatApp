package com.example.chatapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.*
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.net.toUri
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.navigation.CHAT_ARGS
import com.example.chatapp.navigation.CHAT_URI
import com.example.chatapp.receiver.NotificationReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

const val RESULT_KEY = "result_key"
const val FIRST_CHANNEL_ID = "first_channel_id"
const val FIRST_CHANNEL_NAME = "first_channel_name"
const val SECOND_CHANNEL_ID = "second_channel_id"
const val SECOND_CHANNEL_NAME = "second_channel_name"
const val THIRD_CHANNEL_ID = "third_channel_id"
const val THIRD_CHANNEL_NAME = "third_channel_name"

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    @FirstNotificationCompactBuilder
    fun provideFirstNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("message", "Action Clicked")
        }

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            flag
        )

        //click notification
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            "$CHAT_URI/$CHAT_ARGS=From Notification".toUri(),
            context,
            MainActivity::class.java
        )

        val clickPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(1, flag)
        }

        return NotificationCompat.Builder(context, FIRST_CHANNEL_ID)
            .setContentTitle("Welcome")
            .setContentText("Arief Ahmad Alfian")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_PRIVATE)
            .setPublicVersion( //untuk keadaan unlock screen
                NotificationCompat.Builder(context, FIRST_CHANNEL_ID)
                    .setContentTitle("hidden")
                    .setContentText("unlock to see the message.")
                    .build()
            )
            .addAction(0, "Action", pendingIntent) //aksi click button di notif
            .setContentIntent(clickPendingIntent) //aksi click notif deep link
    }

    @Singleton
    @Provides
    @SecondNotificationCompactBuilder
    fun provideSecondNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, SECOND_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(VISIBILITY_PRIVATE)
            .setOngoing(true)
    }

    @Singleton
    @Provides
    @ThirdNotificationCompactBuilder
    fun provideThirdNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        val remoteInput = RemoteInput.Builder(RESULT_KEY)
            .setLabel("Type here...")
            .build()
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0

        val replyIntent = Intent(context, NotificationReceiver::class.java)
        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            replyIntent,
            flag
        )
        val replyAction = NotificationCompat.Action.Builder(
            0,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val person = Person.Builder().setName("Alfian").build()

        val notificationStyle = NotificationCompat.MessagingStyle(person)
            .addMessage("Hi Alfian", System.currentTimeMillis(), person)

        return NotificationCompat.Builder(context, THIRD_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setStyle(notificationStyle)
            .addAction(replyAction)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelFirst = NotificationChannel(
                FIRST_CHANNEL_ID,
                FIRST_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channelSecond = NotificationChannel(
                SECOND_CHANNEL_ID,
                SECOND_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val channelThird = NotificationChannel(
                THIRD_CHANNEL_ID,
                THIRD_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManagerCompat.createNotificationChannel(channelFirst)
            notificationManagerCompat.createNotificationChannel(channelSecond)
            notificationManagerCompat.createNotificationChannel(channelThird)
        }
        return notificationManagerCompat
    }
}

/**
 * suapaya tidak ambigu Notification.Builder
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FirstNotificationCompactBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecondNotificationCompactBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ThirdNotificationCompactBuilder