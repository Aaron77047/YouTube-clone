package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.SeedData
import com.example.data.model.CommentEntity
import com.example.data.model.CreatorAnalytics
import com.example.data.model.GameType
import com.example.data.model.PlayableGame
import com.example.data.model.PlaylistEntity
import com.example.data.model.SubscriptionEntity
import com.example.data.model.VideoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class YouTubeRepository(private val database: AppDatabase) {

    private val videoDao = database.videoDao()
    private val commentDao = database.commentDao()
    private val playlistDao = database.playlistDao()
    private val subscriptionDao = database.subscriptionDao()

    // Ensure database is seeded even if onCreate didn't fire (e.g. pre-existing db)
    suspend fun checkAndSeedInitialData() {
        withContext(Dispatchers.IO) {
            val count = videoDao.getVideoCount()
            if (count == 0) {
                subscriptionDao.insertSubscriptions(SeedData.sampleChannels)
                videoDao.insertVideos(SeedData.sampleVideos)
                commentDao.insertComments(SeedData.sampleComments)
                playlistDao.insertPlaylists(SeedData.samplePlaylists)
            }
        }
    }

    // Video streams
    val allVideos: Flow<List<VideoEntity>> = videoDao.getAllVideos().flowOn(Dispatchers.IO)
    val musicVideos: Flow<List<VideoEntity>> = videoDao.getMusicVideos().flowOn(Dispatchers.IO)
    val downloadedVideos: Flow<List<VideoEntity>> = videoDao.getDownloadedVideos().flowOn(Dispatchers.IO)
    val allPlaylists: Flow<List<PlaylistEntity>> = playlistDao.getAllPlaylists().flowOn(Dispatchers.IO)
    val allSubscriptions: Flow<List<SubscriptionEntity>> = subscriptionDao.getAllSubscriptions().flowOn(Dispatchers.IO)

    fun getVideosByCategory(category: String): Flow<List<VideoEntity>> {
        return if (category == "All") {
            videoDao.getAllVideos().flowOn(Dispatchers.IO)
        } else if (category == "Music") {
            videoDao.getMusicVideos().flowOn(Dispatchers.IO)
        } else if (category == "Downloaded") {
            videoDao.getDownloadedVideos().flowOn(Dispatchers.IO)
        } else {
            videoDao.getVideosByCategory(category).flowOn(Dispatchers.IO)
        }
    }

    fun searchVideos(query: String): Flow<List<VideoEntity>> {
        return videoDao.searchVideos(query).flowOn(Dispatchers.IO)
    }

    fun getCommentsForVideo(videoId: Long): Flow<List<CommentEntity>> {
        return commentDao.getCommentsForVideo(videoId).flowOn(Dispatchers.IO)
    }

    suspend fun getVideoById(id: Long): VideoEntity? = withContext(Dispatchers.IO) {
        videoDao.getVideoById(id)
    }

    suspend fun insertVideo(video: VideoEntity): Long = withContext(Dispatchers.IO) {
        videoDao.insertVideo(video)
    }

    suspend fun incrementViewCount(videoId: Long) = withContext(Dispatchers.IO) {
        videoDao.incrementViewCount(videoId)
    }

    suspend fun toggleLike(videoId: Long, isLiked: Boolean) = withContext(Dispatchers.IO) {
        videoDao.toggleLike(videoId, isLiked)
    }

    suspend fun toggleDislike(videoId: Long, isDisliked: Boolean) = withContext(Dispatchers.IO) {
        videoDao.toggleDislike(videoId, isDisliked)
    }

    suspend fun toggleDownload(videoId: Long, isDownloaded: Boolean) = withContext(Dispatchers.IO) {
        videoDao.updateDownloadStatus(videoId, isDownloaded)
    }

    suspend fun toggleWatchLater(videoId: Long, isSaved: Boolean) = withContext(Dispatchers.IO) {
        videoDao.updateWatchLater(videoId, isSaved)
    }

    suspend fun addComment(videoId: Long, authorName: String, authorAvatarUrl: String, content: String): Long = withContext(Dispatchers.IO) {
        val comment = CommentEntity(
            videoId = videoId,
            authorName = authorName,
            authorAvatarUrl = authorAvatarUrl,
            content = content,
            timestamp = System.currentTimeMillis(),
            likesCount = 0,
            isLikedByMe = false
        )
        commentDao.insertComment(comment)
    }

    suspend fun toggleCommentLike(commentId: Long, isLiked: Boolean) = withContext(Dispatchers.IO) {
        commentDao.toggleCommentLike(commentId, isLiked)
    }

    suspend fun toggleSubscription(channelId: String, isSubscribed: Boolean) = withContext(Dispatchers.IO) {
        subscriptionDao.updateSubscriptionStatus(channelId, isSubscribed)
    }

    suspend fun toggleNotificationBell(channelId: String, hasBell: Boolean) = withContext(Dispatchers.IO) {
        subscriptionDao.updateNotificationBell(channelId, hasBell)
    }

    suspend fun createPlaylist(title: String, description: String, initialVideoId: Long?): Long = withContext(Dispatchers.IO) {
        val playlist = PlaylistEntity(
            title = title,
            description = description,
            videoIds = if (initialVideoId != null) initialVideoId.toString() else "",
            createdAt = System.currentTimeMillis()
        )
        playlistDao.insertPlaylist(playlist)
    }

    suspend fun addVideoToPlaylist(playlistId: Long, videoId: Long) = withContext(Dispatchers.IO) {
        val playlist = playlistDao.getPlaylistById(playlistId) ?: return@withContext
        val currentIds = if (playlist.videoIds.isBlank()) mutableListOf() else playlist.videoIds.split(",").map { it.trim() }.toMutableList()
        val strId = videoId.toString()
        if (!currentIds.contains(strId)) {
            currentIds.add(strId)
            val updated = playlist.copy(videoIds = currentIds.joinToString(","))
            playlistDao.updatePlaylist(updated)
        }
    }

    fun getPlayableGames(): List<PlayableGame> {
        return listOf(
            PlayableGame(
                id = "game_2048",
                title = "2048: Neon Fusion",
                subtitle = "Slide and merge numbers to reach 2048!",
                category = "Puzzle",
                rating = 4.9f,
                playersCount = "1.2M plays",
                gameType = GameType.GAME_2048,
                bannerColorHex = 0xFFEDC22E,
                iconEmoji = "🔢",
                description = "Classic addictive 4x4 sliding block puzzle with cyber neon styling. Swipe UP, DOWN, LEFT, and RIGHT to combine matching tiles.",
                isFeatured = true
            ),
            PlayableGame(
                id = "game_snake",
                title = "Retro Snake 8-Bit",
                subtitle = "Eat glowing power apples and grow your snake!",
                category = "Arcade",
                rating = 4.8f,
                playersCount = "850K plays",
                gameType = GameType.SNAKE,
                bannerColorHex = 0xFF2BA640,
                iconEmoji = "🐍",
                description = "Nostalgic arcade runner. Guide your snake through the pixel grid, avoiding self-collisions and walls as your speed ramps up.",
                isFeatured = true
            ),
            PlayableGame(
                id = "game_speed_tap",
                title = "Speed Tap Blitz",
                subtitle = "Test your reaction time in 30 seconds!",
                category = "Action",
                rating = 4.7f,
                playersCount = "430K plays",
                gameType = GameType.RETRO_TAP,
                bannerColorHex = 0xFFFF0000,
                iconEmoji = "⚡",
                description = "Rapid fire reflex challenge. Tap flashing target pads before they decay to maintain your multiplier combo."
            )
        )
    }

    fun getCreatorAnalytics(): CreatorAnalytics {
        return CreatorAnalytics()
    }
}
