package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.data.local.AppDatabase

class YouTubeApplication : Application() {

    companion object {
        const val PLAYBACK_CHANNEL_ID = "media_playback_channel"
        const val UPLOAD_CHANNEL_ID = "upload_notification_channel"
        lateinit var instance: YouTubeApplication
            private set
    }

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val playbackChannel = NotificationChannel(
                PLAYBACK_CHANNEL_ID,
                "Media Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls and information for currently playing video and music"
            }

            val uploadChannel = NotificationChannel(
                UPLOAD_CHANNEL_ID,
                "Video Upload Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for new video uploads from subscriptions"
            }

            notificationManager.createNotificationChannel(playbackChannel)
            notificationManager.createNotificationChannel(uploadChannel)
        }
    }
}
