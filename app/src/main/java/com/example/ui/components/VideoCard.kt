package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.VideoEntity
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.theme.YouTubeRed

@Composable
fun VideoCard(
    video: VideoEntity,
    onClick: () -> Unit,
    onSaveToWatchLater: () -> Unit,
    onSaveToPlaylist: () -> Unit,
    onToggleDownload: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(OledBlack)
            .clickable(onClick = onClick)
            .padding(bottom = 16.dp)
            .testTag("video_card_${video.id}")
    ) {
        // Thumbnail container with 16:9 ratio
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(DarkSurfaceElevated)
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Duration Pill
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xCC000000))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = formatDuration(video.durationSeconds),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Status badges (Music / Downloaded)
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (video.isMusic) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(YouTubeRed)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("MUSIC", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                if (video.isDownloaded) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xCC000000))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DownloadDone, contentDescription = null, tint = VerifiedBlue, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("OFFLINE", color = VerifiedBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Metadata row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Creator Avatar
            AsyncImage(
                model = video.creatorAvatarUrl,
                contentDescription = video.creatorName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceElevated)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Title & Channel info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = video.creatorName,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = VerifiedBlue,
                        modifier = Modifier.size(12.dp)
                    )
                }

                Text(
                    text = "${formatViews(video.viewsCount)} • ${formatRelativeTime(video.uploadTimestamp)}",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            // Overflow Menu
            Box {
                IconButton(
                    onClick = { showMenu = true },
                    modifier = Modifier.size(32.dp).testTag("video_menu_${video.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Options",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(DarkSurfaceElevated)
                ) {
                    DropdownMenuItem(
                        text = { Text("Save to Watch Later", color = TextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.WatchLater, null, tint = TextPrimary) },
                        onClick = {
                            showMenu = false
                            onSaveToWatchLater()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Save to Playlist", color = TextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Outlined.PlaylistAdd, null, tint = TextPrimary) },
                        onClick = {
                            showMenu = false
                            onSaveToPlaylist()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                if (video.isDownloaded) "Remove Download" else "Download Video",
                                color = TextPrimary,
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.DownloadDone, null, tint = TextPrimary) },
                        onClick = {
                            showMenu = false
                            onToggleDownload()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Share", color = TextPrimary, fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Default.Share, null, tint = TextPrimary) },
                        onClick = {
                            showMenu = false
                            onShare()
                        }
                    )
                }
            }
        }
    }
}

fun formatDuration(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    val hours = mins / 60
    val remainingMins = mins % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, remainingMins, secs)
    } else {
        String.format("%d:%02d", remainingMins, secs)
    }
}

fun formatViews(views: Long): String {
    return when {
        views >= 1_000_000 -> String.format("%.1fM views", views / 1_000_000.0)
        views >= 1_000 -> String.format("%.1fK views", views / 1_000.0)
        else -> "$views views"
    }
}

fun formatRelativeTime(timestamp: Long): String {
    val diffMs = System.currentTimeMillis() - timestamp
    val hours = diffMs / (1000 * 60 * 60)
    val days = hours / 24
    return when {
        days > 30 -> "${days / 30} months ago"
        days > 0 -> "${days}d ago"
        hours > 0 -> "${hours}h ago"
        else -> "Just now"
    }
}
