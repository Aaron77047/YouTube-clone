package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.SubscriptionEntity
import com.example.data.model.VideoEntity
import com.example.ui.components.VideoCard
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.theme.YouTubeRed
import com.example.viewmodel.YouTubeViewModel

@Composable
fun SubscriptionsScreen(
    viewModel: YouTubeViewModel,
    subscriptions: List<SubscriptionEntity>,
    allVideos: List<VideoEntity>,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedChannelId by remember { mutableStateOf<String?>(null) }

    val displayedVideos = if (selectedChannelId == null) {
        allVideos
    } else {
        allVideos.filter { it.channelId == selectedChannelId }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
    ) {
        // Horizontal Channel Story Avatars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(OledBlack)
                .padding(vertical = 12.dp, horizontal = 12.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "All" Circle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { selectedChannelId = null }
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(if (selectedChannelId == null) YouTubeRed else DarkSurfaceElevated)
                        .border(
                            2.dp,
                            if (selectedChannelId == null) YouTubeRed else DarkSurfaceBorder,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("All", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("All", color = TextSecondary, fontSize = 11.sp)
            }

            // Channels
            subscriptions.forEach { channel ->
                val isSelected = channel.channelId == selectedChannelId
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { selectedChannelId = channel.channelId }
                        .testTag("sub_channel_${channel.channelId}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = if (isSelected) YouTubeRed else if (channel.isSubscribed) VerifiedBlue else Color.Transparent,
                                shape = CircleShape
                            )
                            .padding(2.dp)
                    ) {
                        AsyncImage(
                            model = channel.avatarUrl,
                            contentDescription = channel.channelName,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(DarkSurfaceElevated)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = channel.channelName.split(" ").first(),
                        color = if (isSelected) TextPrimary else TextSecondary,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        // Channel info bar if a specific channel is selected
        if (selectedChannelId != null) {
            val currentChannel = subscriptions.find { it.channelId == selectedChannelId }
            if (currentChannel != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(currentChannel.channelName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        Text("${currentChannel.subscriberCount} • ${currentChannel.videoCount} videos", color = TextSecondary, fontSize = 11.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(DarkSurfaceElevated)
                                .clickable { viewModel.toggleSubscription(currentChannel) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (currentChannel.isSubscribed) "Subscribed" else "Subscribe",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(onClick = { viewModel.toggleNotificationBell(currentChannel) }) {
                            Icon(
                                imageVector = if (currentChannel.hasNotificationBell) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "Bell",
                                tint = if (currentChannel.hasNotificationBell) YouTubeRed else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Subscriptions Video Feed
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp)
        ) {
            items(displayedVideos, key = { it.id }) { video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onSaveToWatchLater = { viewModel.toggleWatchLater(video.id, true) },
                    onSaveToPlaylist = {
                        val playlists = viewModel.allPlaylists.value
                        if (playlists.isNotEmpty()) {
                            viewModel.addVideoToPlaylist(playlists.first().id, video.id)
                        }
                    },
                    onToggleDownload = { viewModel.toggleDownload(video) },
                    onShare = { viewModel.shareVideo(context, video) }
                )
            }
        }
    }
}
