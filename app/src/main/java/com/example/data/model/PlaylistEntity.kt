package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val videoIds: String = "", // Comma-separated video IDs
    val createdAt: Long = System.currentTimeMillis(),
    val isPrivate: Boolean = true,
    val coverThumbnailUrl: String = ""
)
