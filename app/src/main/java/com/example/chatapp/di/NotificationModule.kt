package com.example.chatapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.VISIBILITY_PRIVATE
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
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

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Singleton
    @Provides
    @MainNotificationCompactBuilder
    fun provideNotificationBuilder(
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

        return NotificationCompat.Builder(context, "Main Channel ID")
            .setContentTitle("Welcome")
            .setContentText("Arief Ahmad Alfian")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(VISIBILITY_PRIVATE)
            .setPublicVersion( //untuk keadaan unlock screen
                NotificationCompat.Builder(context, "Main Channel ID")
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
        return NotificationCompat.Builder(context, "Second Channel ID")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(VISIBILITY_PRIVATE)
            .setOngoing(true)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "Main Channel ID",
                "Main Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channelSecond = NotificationChannel(
                "Second Channel ID",
                "Second Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManagerCompat.createNotificationChannel(channel)
            notificationManagerCompat.createNotificationChannel(channelSecond)
        }
        return notificationManagerCompat
    }
}

/**
 * suapaya tidak ambigu Notification.Builder
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainNotificationCompactBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SecondNotificationCompactBuilder