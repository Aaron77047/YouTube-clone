package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.YouTubeApplication
import com.example.data.model.CommentEntity
import com.example.data.model.CreatorAnalytics
import com.example.data.model.PlayableGame
import com.example.data.model.PlaylistEntity
import com.example.data.model.SubscriptionEntity
import com.example.data.model.VideoEntity
import com.example.data.repository.YouTubeRepository
import com.example.service.MediaPlaybackService
import com.example.service.PlaybackState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    HOME,
    PLAYABLES,
    UPLOAD,
    SUBSCRIPTIONS,
    LIBRARY
}

data class InAppNotification(
    val id: Long,
    val title: String,
    val message: String,
    val channelAvatar: String,
    val timestamp: Long = System.currentTimeMillis(),
    val videoId: Long? = null
)

class YouTubeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = YouTubeRepository((application as YouTubeApplication).database)

    // Background playback state from service
    val playbackState: StateFlow<PlaybackState> = MediaPlaybackService.playbackState

    // Navigation and UI state
    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _playablesSearchQuery = MutableStateFlow("")
    val playablesSearchQuery: StateFlow<String> = _playablesSearchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive: StateFlow<Boolean> = _isSearchActive.asStateFlow()

    // Currently playing media
    private val _currentVideo = MutableStateFlow<VideoEntity?>(null)
    val currentVideo: StateFlow<VideoEntity?> = _currentVideo.asStateFlow()

    private val _isPlayerExpanded = MutableStateFlow(false)
    val isPlayerExpanded: StateFlow<Boolean> = _isPlayerExpanded.asStateFlow()

    private val _isFloatingPipActive = MutableStateFlow(false)
    val isFloatingPipActive: StateFlow<Boolean> = _isFloatingPipActive.asStateFlow()

    private val _isInPipMode = MutableStateFlow(false)
    val isInPipMode: StateFlow<Boolean> = _isInPipMode.asStateFlow()

    private val _isBackgroundPlayEnabled = MutableStateFlow(true)
    val isBackgroundPlayEnabled: StateFlow<Boolean> = _isBackgroundPlayEnabled.asStateFlow()

    // Active Playable mini-game
    private val _activePlayableGame = MutableStateFlow<PlayableGame?>(null)
    val activePlayableGame: StateFlow<PlayableGame?> = _activePlayableGame.asStateFlow()

    // Creator studio screen toggle
    private val _isCreatorStudioOpen = MutableStateFlow(false)
    val isCreatorStudioOpen: StateFlow<Boolean> = _isCreatorStudioOpen.asStateFlow()

    // Notifications sheet
    private val _isNotificationsOpen = MutableStateFlow(false)
    val isNotificationsOpen: StateFlow<Boolean> = _isNotificationsOpen.asStateFlow()

    private val _notifications = MutableStateFlow<List<InAppNotification>>(emptyList())
    val notifications: StateFlow<List<InAppNotification>> = _notifications.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Comments for active video
    private val _comments = MutableStateFlow<List<CommentEntity>>(emptyList())
    val comments: StateFlow<List<CommentEntity>> = _comments.asStateFlow()

    // Repository flows
    val allVideos: StateFlow<List<VideoEntity>> = repository.allVideos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPlaylists: StateFlow<List<PlaylistEntity>> = repository.allPlaylists
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSubscriptions: StateFlow<List<SubscriptionEntity>> = repository.allSubscriptions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val playableGames: List<PlayableGame> = repository.getPlayableGames()

    val creatorAnalytics: CreatorAnalytics = repository.getCreatorAnalytics()

    // Filtered Video Feed
    val filteredVideos: StateFlow<List<VideoEntity>> = combine(
        allVideos,
        _selectedCategory,
        _searchQuery
    ) { videos, category, query ->
        var result = videos
        if (category == "Music") {
            result = result.filter { it.isMusic }
        } else if (category == "Downloaded") {
            result = result.filter { it.isDownloaded }
        } else if (category != "All") {
            result = result.filter { it.category.equals(category, ignoreCase = true) }
        }

        if (query.isNotBlank()) {
            result = result.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.creatorName.contains(query, ignoreCase = true) ||
                        it.tags.contains(query, ignoreCase = true)
            }
        }
        result
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.checkAndSeedInitialData()
            seedNotifications()
        }
    }

    private fun seedNotifications() {
        _notifications.value = listOf(
            InAppNotification(
                id = 1,
                title = "Marques Brownlee uploaded:",
                message = "Building the Ultimate Minimalist Android Setup in 2026",
                channelAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
                videoId = 1
            ),
            InAppNotification(
                id = 2,
                title = "Lofi Girl went live:",
                message = "Midnight Chill Beats | Lofi Hip Hop Stream for Deep Focus",
                channelAvatar = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=150&auto=format&fit=crop&q=80",
                videoId = 2
            ),
            InAppNotification(
                id = 3,
                title = "Veritasium premiered:",
                message = "The Quantum Computing Breakthrough Nobody Expected",
                channelAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
                videoId = 3
            )
        )
    }

    fun setTab(tab: MainTab) {
        _currentTab.value = tab
        if (tab != MainTab.PLAYABLES) {
            _activePlayableGame.value = null
        }
        _isCreatorStudioOpen.value = false
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setPlayablesSearchQuery(query: String) {
        _playablesSearchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }

    fun openCreatorStudio(open: Boolean) {
        _isCreatorStudioOpen.value = open
    }

    fun openNotifications(open: Boolean) {
        _isNotificationsOpen.value = open
    }

    fun openPlayableGame(game: PlayableGame?) {
        _activePlayableGame.value = game
    }

    fun playVideo(video: VideoEntity, startExpanded: Boolean = true) {
        _currentVideo.value = video
        _isPlayerExpanded.value = startExpanded
        _isFloatingPipActive.value = false

        // Load comments for this video
        viewModelScope.launch {
            repository.getCommentsForVideo(video.id).collect {
                _comments.value = it
            }
        }

        // Increment views
        viewModelScope.launch {
            repository.incrementViewCount(video.id)
        }

        // Start background media playback service
        val context = getApplication<Application>().applicationContext
        MediaPlaybackService.startService(
            context = context,
            mediaUrl = video.mediaUrl,
            title = video.title,
            artist = video.creatorName
        )
    }

    fun setPlayerExpanded(expanded: Boolean) {
        _isPlayerExpanded.value = expanded
        if (expanded) {
            _isFloatingPipActive.value = false
        }
    }

    fun setFloatingPipActive(active: Boolean) {
        _isFloatingPipActive.value = active
        if (active) {
            _isPlayerExpanded.value = false
        }
    }

    fun setInPipMode(inPip: Boolean) {
        _isInPipMode.value = inPip
    }

    fun togglePlayPause() {
        val context = getApplication<Application>().applicationContext
        val state = playbackState.value
        if (state.isPlaying) {
            MediaPlaybackService.sendAction(context, MediaPlaybackService.ACTION_PAUSE)
        } else {
            MediaPlaybackService.sendAction(context, MediaPlaybackService.ACTION_PLAY)
        }
    }

    fun seekTo(positionMs: Int) {
        val context = getApplication<Application>().applicationContext
        val service = MediaPlaybackService()
        service.seekTo(positionMs)
    }

    fun seekForward() {
        val context = getApplication<Application>().applicationContext
        MediaPlaybackService.sendAction(context, MediaPlaybackService.ACTION_SEEK_FORWARD)
    }

    fun seekBackward() {
        val context = getApplication<Application>().applicationContext
        MediaPlaybackService.sendAction(context, MediaPlaybackService.ACTION_SEEK_BACK)
    }

    fun closePlayer() {
        val context = getApplication<Application>().applicationContext
        MediaPlaybackService.sendAction(context, MediaPlaybackService.ACTION_STOP)
        _currentVideo.value = null
        _isPlayerExpanded.value = false
        _isFloatingPipActive.value = false
    }

    fun toggleBackgroundPlay() {
        _isBackgroundPlayEnabled.value = !_isBackgroundPlayEnabled.value
        viewModelScope.launch {
            _toastEvent.emit(if (_isBackgroundPlayEnabled.value) "Background play enabled" else "Background play disabled")
        }
    }

    fun toggleLike(video: VideoEntity) {
        val newLiked = !video.isLiked
        viewModelScope.launch {
            repository.toggleLike(video.id, newLiked)
            _currentVideo.value = _currentVideo.value?.copy(
                isLiked = newLiked,
                isDisliked = false,
                likesCount = if (newLiked) video.likesCount + 1 else (video.likesCount - 1).coerceAtLeast(0)
            )
        }
    }

    fun toggleDislike(video: VideoEntity) {
        val newDisliked = !video.isDisliked
        viewModelScope.launch {
            repository.toggleDislike(video.id, newDisliked)
            _currentVideo.value = _currentVideo.value?.copy(
                isDisliked = newDisliked,
                isLiked = false
            )
        }
    }

    fun toggleWatchLater(videoId: Long, isSaved: Boolean) {
        viewModelScope.launch {
            repository.toggleWatchLater(videoId, isSaved)
            _toastEvent.emit(if (isSaved) "Saved to Watch Later" else "Removed from Watch Later")
        }
    }

    fun toggleDownload(video: VideoEntity) {
        val targetState = !video.isDownloaded
        viewModelScope.launch {
            repository.toggleDownload(video.id, targetState)
            _currentVideo.value = _currentVideo.value?.copy(isDownloaded = targetState)
            _toastEvent.emit(if (targetState) "Video cached for offline viewing" else "Removed from offline downloads")
        }
    }

    fun shareVideo(context: Context, video: VideoEntity) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Check out \"${video.title}\" by ${video.creatorName} on YouTube!\n${video.mediaUrl}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share video via")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    fun addComment(videoId: Long, content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.addComment(
                videoId = videoId,
                authorName = "You (Creator)",
                authorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                content = content
            )
            _toastEvent.emit("Comment posted!")
        }
    }

    fun toggleCommentLike(comment: CommentEntity) {
        val newLiked = !comment.isLikedByMe
        viewModelScope.launch {
            repository.toggleCommentLike(comment.id, newLiked)
        }
    }

    fun toggleSubscription(channel: SubscriptionEntity) {
        val newSubscribed = !channel.isSubscribed
        viewModelScope.launch {
            repository.toggleSubscription(channel.channelId, newSubscribed)
            _toastEvent.emit(if (newSubscribed) "Subscribed to ${channel.channelName}" else "Unsubscribed from ${channel.channelName}")
        }
    }

    fun toggleNotificationBell(channel: SubscriptionEntity) {
        val newBell = !channel.hasNotificationBell
        viewModelScope.launch {
            repository.toggleNotificationBell(channel.channelId, newBell)
            _toastEvent.emit(if (newBell) "All notifications on for ${channel.channelName}" else "Notifications silenced")
        }
    }

    fun createPlaylist(title: String, description: String, initialVideoId: Long?) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.createPlaylist(title, description, initialVideoId)
            _toastEvent.emit("Playlist \"$title\" created!")
        }
    }

    fun addVideoToPlaylist(playlistId: Long, videoId: Long) {
        viewModelScope.launch {
            repository.addVideoToPlaylist(playlistId, videoId)
            _toastEvent.emit("Saved to playlist!")
        }
    }

    fun uploadMedia(
        title: String,
        description: String,
        category: String,
        tags: String,
        isMusic: Boolean,
        mediaUri: Uri?
    ) {
        if (title.isBlank()) {
            viewModelScope.launch { _toastEvent.emit("Please enter a title for your upload") }
            return
        }

        viewModelScope.launch {
            val mediaUrl = mediaUri?.toString()
                ?: if (isMusic) "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
                else "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

            val defaultThumb = if (isMusic) {
                "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80"
            } else {
                "https://images.unsplash.com/photo-1574717024653-61fd2cf4d44d?w=800&auto=format&fit=crop&q=80"
            }

            val newVideo = VideoEntity(
                title = title,
                description = description,
                creatorName = "You (Creator)",
                creatorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                channelId = "c_my_channel",
                mediaUrl = mediaUrl,
                thumbnailUrl = defaultThumb,
                durationSeconds = if (isMusic) 240 else 480,
                viewsCount = 1,
                likesCount = 0,
                uploadTimestamp = System.currentTimeMillis(),
                isMusic = isMusic,
                category = category,
                tags = tags
            )

            val newId = repository.insertVideo(newVideo)

            // Add to real-time notifications
            val newNotification = InAppNotification(
                id = System.currentTimeMillis(),
                title = "New upload live on YouTube:",
                message = title,
                channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
                videoId = newId
            )
            _notifications.value = listOf(newNotification) + _notifications.value

            _toastEvent.emit(if (isMusic) "Music track published successfully!" else "Video uploaded successfully!")
            _currentTab.value = MainTab.HOME
        }
    }
}
