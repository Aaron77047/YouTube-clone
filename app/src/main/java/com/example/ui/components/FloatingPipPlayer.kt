package com.example.ui.components

import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.model.VideoEntity
import com.example.service.PlaybackState
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.YouTubeRed
import kotlin.math.roundToInt

@Composable
fun FloatingPipPlayer(
    video: VideoEntity,
    playbackState: PlaybackState,
    onExpand: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onEnterSystemPip: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(100f) }
    var showOverlayControls by remember { mutableStateOf(false) }

    val currentSec = (playbackState.currentPositionMs / 1000).coerceAtLeast(0)
    val totalSec = video.durationSeconds.coerceAtLeast(1)
    val progress = (currentSec.toFloat() / totalSec.toFloat()).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .width(220.dp)
            .aspectRatio(16f / 9.5f)
            .shadow(12.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .border(1.5.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { showOverlayControls = !showOverlayControls },
                    onDoubleTap = { onExpand() }
                )
            }
            .testTag("floating_pip_player_container")
    ) {
        // Video View Surface
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    setVideoPath(video.mediaUrl)
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

        // Buffering Indicator
        if (playbackState.isBuffering) {
            CircularProgressIndicator(
                color = YouTubeRed,
                strokeWidth = 2.dp,
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.Center)
            )
        }

        // Overlay Controls
        AnimatedVisibility(
            visible = showOverlayControls,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000))
                    .padding(4.dp)
            ) {
                // Top control actions (Expand, System PiP, Close)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Title
                    Text(
                        text = video.title,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // System PiP trigger
                        IconButton(
                            onClick = onEnterSystemPip,
                            modifier = Modifier.size(24.dp).testTag("floating_pip_system_pip_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PictureInPictureAlt,
                                contentDescription = "System PiP",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        // Expand to full screen
                        IconButton(
                            onClick = onExpand,
                            modifier = Modifier.size(24.dp).testTag("floating_pip_expand_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Expand Fullscreen",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Close player
                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.size(24.dp).testTag("floating_pip_close_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                // Center Play/Pause button
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xAA000000))
                        .clickable(onClick = onTogglePlayPause)
                        .testTag("floating_pip_play_pause_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Drag indicator label
                Text(
                    text = "Drag to move • Double-tap full",
                    color = Color(0xBBFFFFFF),
                    fontSize = 8.sp,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 4.dp, bottom = 4.dp)
                )
            }
        }

        // Mini Bottom Scrub Bar
        LinearProgressIndicator(
            progress = { progress },
            color = YouTubeRed,
            trackColor = Color(0x44FFFFFF),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(2.dp)
        )
    }
}
