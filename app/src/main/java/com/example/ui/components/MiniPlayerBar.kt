package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.service.PlaybackState
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.YouTubeRed

@Composable
fun MiniPlayerBar(
    video: VideoEntity,
    playbackState: PlaybackState,
    onExpand: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentSec = (playbackState.currentPositionMs / 1000).coerceAtLeast(0)
    val totalSec = video.durationSeconds.coerceAtLeast(1)
    val progress = (currentSec.toFloat() / totalSec.toFloat()).coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(DarkSurfaceElevated)
            .clickable(onClick = onExpand)
            .testTag("mini_player_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight(0.85f)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Metadata
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = video.creatorName,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }

            // Play/Pause Action
            IconButton(
                onClick = onTogglePlayPause,
                modifier = Modifier.size(36.dp).testTag("mini_player_play_pause")
            ) {
                Icon(
                    imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Close Action
            IconButton(
                onClick = onClose,
                modifier = Modifier.size(36.dp).testTag("mini_player_close")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = TextPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Mini Scrub Bar
        LinearProgressIndicator(
            progress = { progress },
            color = YouTubeRed,
            trackColor = Color(0x33FFFFFF),
            modifier = Modifier.fillMaxWidth().height(2.dp)
        )
    }
}
