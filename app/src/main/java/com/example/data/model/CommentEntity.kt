package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val videoId: Long,
    val authorName: String,
    val authorAvatarUrl: String,
    val content: String,
    val timestamp: Long,
    val likesCount: Int = 0,
    val isLikedByMe: Boolean = false
)
