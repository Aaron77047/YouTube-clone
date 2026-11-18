package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val creatorName: String,
    val creatorAvatarUrl: String,
    val channelId: String,
    val mediaUrl: String, // Web video URL, local content URI or raw stream
    val thumbnailUrl: String,
    val durationSeconds: Int,
    val viewsCount: Long,
    val likesCount: Long,
    val uploadTimestamp: Long,
    val isMusic: Boolean = false,
    val isDownloaded: Boolean = false,
    val category: String = "General",
    val tags: String = "",
    val quality: String = "1080p",
    val isLiked: Boolean = false,
    val isDisliked: Boolean = false,
    val isSavedToWatchLater: Boolean = false
)
