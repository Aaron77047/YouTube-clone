package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CommentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE videoId = :videoId ORDER BY timestamp DESC")
    fun getCommentsForVideo(videoId: Long): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("UPDATE comments SET likesCount = likesCount + (CASE WHEN :isLiked THEN 1 ELSE -1 END), isLikedByMe = :isLiked WHERE id = :commentId")
    suspend fun toggleCommentLike(commentId: Long, isLiked: Boolean)

    @Delete
    suspend fun deleteComment(comment: CommentEntity)
}
