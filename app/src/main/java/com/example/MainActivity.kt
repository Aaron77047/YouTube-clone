package com.example

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.VideoEntity
import com.example.ui.components.FloatingPipPlayer
import com.example.ui.components.MiniPlayerBar
import com.example.ui.components.NotificationDrawer
import com.example.ui.components.VideoPlayerSheet
import com.example.ui.components.YouTubeBottomNavBar
import com.example.ui.components.YouTubeTopAppBar
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.screens.PlayablesScreen
import com.example.ui.screens.SubscriptionsScreen
import com.example.ui.screens.UploadScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OledBlack
import com.example.viewmodel.MainTab
import com.example.viewmodel.YouTubeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: YouTubeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                YouTubeApp(
                    viewModel = viewModel,
                    onEnterSystemPip = { enterSystemPipMode() }
                )
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // If a video is actively loaded/playing, automatically transition to System PiP on Home press
        if (viewModel.currentVideo.value != null) {
            enterSystemPipMode()
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.setInPipMode(isInPictureInPictureMode)
    }

    fun enterSystemPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val paramsBuilder = PictureInPictureParams.Builder()
                    .setAspectRatio(Rational(16, 9))

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    paramsBuilder.setAutoEnterEnabled(true)
                }

                enterPictureInPictureMode(paramsBuilder.build())
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback to in-app floating PiP if OS PiP is unsupported or disabled
                viewModel.setFloatingPipActive(true)
            }
        } else {
            // Android < 8.0 fallback to in-app floating PiP
            viewModel.setFloatingPipActive(true)
        }
    }
}

@Composable
fun YouTubeApp(
    viewModel: YouTubeViewModel,
    onEnterSystemPip: () -> Unit
) {
    val context = LocalContext.current

    // Observe ViewModel States
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()

    val currentVideo by viewModel.currentVideo.collectAsStateWithLifecycle()
    val isPlayerExpanded by viewModel.isPlayerExpanded.collectAsStateWithLifecycle()
    val isFloatingPipActive by viewModel.isFloatingPipActive.collectAsStateWithLifecycle()
    val isInPipMode by viewModel.isInPipMode.collectAsStateWithLifecycle()
    val isBackgroundPlayEnabled by viewModel.isBackgroundPlayEnabled.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

    val comments by viewModel.comments.collectAsStateWithLifecycle()
    val filteredVideos by viewModel.filteredVideos.collectAsStateWithLifecycle()
    val allVideos by viewModel.allVideos.collectAsStateWithLifecycle()
    val allPlaylists by viewModel.allPlaylists.collectAsStateWithLifecycle()
    val allSubscriptions by viewModel.allSubscriptions.collectAsStateWithLifecycle()

    val activePlayableGame by viewModel.activePlayableGame.collectAsStateWithLifecycle()
    val isNotificationsOpen by viewModel.isNotificationsOpen.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    // Handle Toast Events
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Comprehensive BackHandler logic
    val isCreatorStudioOpen = viewModel.isCreatorStudioOpen.value
    BackHandler(
        enabled = isPlayerExpanded || isFloatingPipActive || activePlayableGame != null || isCreatorStudioOpen || isSearchActive || currentTab != MainTab.HOME
    ) {
        when {
            isPlayerExpanded -> viewModel.setPlayerExpanded(false)
            isFloatingPipActive -> viewModel.setFloatingPipActive(false)
            activePlayableGame != null -> viewModel.openPlayableGame(null)
            isCreatorStudioOpen -> viewModel.openCreatorStudio(false)
            isSearchActive -> {
                viewModel.setSearchActive(false)
                viewModel.setSearchQuery("")
            }
            currentTab != MainTab.HOME -> viewModel.setTab(MainTab.HOME)
        }
    }

    // When the OS PiP mode is active, display ONLY the clean video playback stream edge-to-edge
    if (isInPipMode && currentVideo != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoPath(currentVideo!!.mediaUrl)
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            start()
                        }
                    }
                },
                update = { videoView ->
                    if (playbackState.isPlaying) {
                        if (!videoView.isPlaying) videoView.start()
                    } else {
                        if (videoView.isPlaying) videoView.pause()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OledBlack)
    ) {
        Scaffold(
            topBar = {
                if (!isPlayerExpanded && activePlayableGame == null && !isCreatorStudioOpen) {
                    Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                        YouTubeTopAppBar(
                            isSearchActive = isSearchActive,
                            searchQuery = searchQuery,
                            notificationCount = notifications.size,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            onToggleSearch = { viewModel.setSearchActive(it) },
                            onNotificationsClick = { viewModel.openNotifications(true) },
                            onAvatarClick = {
                                viewModel.setTab(MainTab.LIBRARY)
                                viewModel.openCreatorStudio(true)
                            }
                        )
                    }
                }
            },
            bottomBar = {
                if (!isPlayerExpanded && activePlayableGame == null) {
                    Column {
                        // Persistent Docked MiniPlayerBar if video is active, collapsed, and not in floating PiP
                        if (currentVideo != null && !isFloatingPipActive) {
                            MiniPlayerBar(
                                video = currentVideo!!,
                                playbackState = playbackState,
                                onExpand = { viewModel.setPlayerExpanded(true) },
                                onTogglePlayPause = { viewModel.togglePlayPause() },
                                onClose = { viewModel.closePlayer() }
                            )
                        }

                        YouTubeBottomNavBar(
                            currentTab = currentTab,
                            onTabSelected = { viewModel.setTab(it) }
                        )
                    }
                }
            },
            containerColor = OledBlack
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentTab) {
                    MainTab.HOME -> {
                        HomeScreen(
                            viewModel = viewModel,
                            videos = filteredVideos,
                            selectedCategory = selectedCategory,
                            onVideoClick = { video -> viewModel.playVideo(video, startExpanded = true) }
                        )
                    }
                    MainTab.PLAYABLES -> {
                        PlayablesScreen(
                            viewModel = viewModel,
                            games = viewModel.playableGames,
                            activeGame = activePlayableGame
                        )
                    }
                    MainTab.UPLOAD -> {
                        UploadScreen(viewModel = viewModel)
                    }
                    MainTab.SUBSCRIPTIONS -> {
                        SubscriptionsScreen(
                            viewModel = viewModel,
                            subscriptions = allSubscriptions,
                            allVideos = allVideos,
                            onVideoClick = { video -> viewModel.playVideo(video, startExpanded = true) }
                        )
                    }
                    MainTab.LIBRARY -> {
                        LibraryScreen(
                            viewModel = viewModel,
                            allVideos = allVideos,
                            playlists = allPlaylists,
                            isBackgroundPlayEnabled = isBackgroundPlayEnabled,
                            onVideoClick = { video -> viewModel.playVideo(video, startExpanded = true) }
                        )
                    }
                }

                // In-App Floating PiP Window (draggable over any tab content)
                if (isFloatingPipActive && currentVideo != null && !isPlayerExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        FloatingPipPlayer(
                            video = currentVideo!!,
                            playbackState = playbackState,
                            onExpand = { viewModel.setPlayerExpanded(true) },
                            onTogglePlayPause = { viewModel.togglePlayPause() },
                            onEnterSystemPip = onEnterSystemPip,
                            onClose = { viewModel.closePlayer() }
                        )
                    }
                }
            }
        }

        // Full-screen Video Player Sheet
        AnimatedVisibility(
            visible = isPlayerExpanded && currentVideo != null,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier.fillMaxSize()
        ) {
            currentVideo?.let { activeVid ->
                val channel = allSubscriptions.find { it.channelId == activeVid.channelId }
                VideoPlayerSheet(
                    video = activeVid,
                    playbackState = playbackState,
                    isBackgroundPlayEnabled = isBackgroundPlayEnabled,
                    comments = comments,
                    channel = channel,
                    onCollapse = { viewModel.setPlayerExpanded(false) },
                    onTogglePlayPause = { viewModel.togglePlayPause() },
                    onSeekForward = { viewModel.seekForward() },
                    onSeekBackward = { viewModel.seekBackward() },
                    onSeekTo = { posMs -> viewModel.seekTo(posMs) },
                    onToggleBackgroundPlay = { viewModel.toggleBackgroundPlay() },
                    onToggleLike = { viewModel.toggleLike(activeVid) },
                    onToggleDislike = { viewModel.toggleDislike(activeVid) },
                    onToggleDownload = { viewModel.toggleDownload(activeVid) },
                    onShare = { viewModel.shareVideo(context, activeVid) },
                    onSaveToPlaylist = {
                        if (allPlaylists.isNotEmpty()) {
                            viewModel.addVideoToPlaylist(allPlaylists.first().id, activeVid.id)
                        }
                    },
                    onToggleSubscription = {
                        if (channel != null) viewModel.toggleSubscription(channel)
                    },
                    onToggleBell = {
                        if (channel != null) viewModel.toggleNotificationBell(channel)
                    },
                    onAddComment = { text -> viewModel.addComment(activeVid.id, text) },
                    onToggleCommentLike = { comment -> viewModel.toggleCommentLike(comment) },
                    onEnterSystemPip = onEnterSystemPip,
                    onToggleFloatingPip = { viewModel.setFloatingPipActive(true) },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
                )
            }
        }

        // Real-time Notification Drawer
        if (isNotificationsOpen) {
            NotificationDrawer(
                notifications = notifications,
                onNotificationClick = { videoId ->
                    if (videoId != null) {
                        val videoToPlay = allVideos.find { it.id == videoId }
                        if (videoToPlay != null) {
                            viewModel.playVideo(videoToPlay, startExpanded = true)
                        }
                    }
                },
                onClose = { viewModel.openNotifications(false) }
            )
        }
    }
}
