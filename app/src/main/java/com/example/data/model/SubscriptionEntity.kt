package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionEntity(
    @PrimaryKey
    val channelId: String,
    val channelName: String,
    val avatarUrl: String,
    val subscriberCount: String,
    val isSubscribed: Boolean = true,
    val hasNotificationBell: Boolean = true,
    val channelBio: String = "",
    val videoCount: Int = 0
)
