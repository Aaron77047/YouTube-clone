package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.VideoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM videos ORDER BY uploadTimestamp DESC")
    fun getAllVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isMusic = 1 ORDER BY uploadTimestamp DESC")
    fun getMusicVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE isDownloaded = 1 ORDER BY uploadTimestamp DESC")
    fun getDownloadedVideos(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE category = :category ORDER BY uploadTimestamp DESC")
    fun getVideosByCategory(category: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR creatorName LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%'")
    fun searchVideos(query: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE channelId = :channelId ORDER BY uploadTimestamp DESC")
    fun getVideosByChannel(channelId: String): Flow<List<VideoEntity>>

    @Query("SELECT * FROM videos WHERE id = :id LIMIT 1")
    suspend fun getVideoById(id: Long): VideoEntity?

    @Query("SELECT * FROM videos WHERE id IN (:ids)")
    fun getVideosByIds(ids: List<Long>): Flow<List<VideoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideos(videos: List<VideoEntity>)

    @Update
    suspend fun updateVideo(video: VideoEntity)

    @Delete
    suspend fun deleteVideo(video: VideoEntity)

    @Query("UPDATE videos SET viewsCount = viewsCount + 1 WHERE id = :videoId")
    suspend fun incrementViewCount(videoId: Long)

    @Query("UPDATE videos SET isLiked = :isLiked, isDisliked = 0, likesCount = likesCount + (CASE WHEN :isLiked THEN 1 ELSE -1 END) WHERE id = :videoId")
    suspend fun toggleLike(videoId: Long, isLiked: Boolean)

    @Query("UPDATE videos SET isDisliked = :isDisliked, isLiked = 0 WHERE id = :videoId")
    suspend fun toggleDislike(videoId: Long, isDisliked: Boolean)

    @Query("UPDATE videos SET isDownloaded = :isDownloaded WHERE id = :videoId")
    suspend fun updateDownloadStatus(videoId: Long, isDownloaded: Boolean)

    @Query("UPDATE videos SET isSavedToWatchLater = :isSaved WHERE id = :videoId")
    suspend fun updateWatchLater(videoId: Long, isSaved: Boolean)

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun getVideoCount(): Int
}
