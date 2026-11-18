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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.model.PlaylistEntity
import com.example.data.model.VideoEntity
import com.example.ui.components.formatDuration
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.theme.YouTubeRed
import com.example.viewmodel.YouTubeViewModel

@Composable
fun LibraryScreen(
    viewModel: YouTubeViewModel,
    allVideos: List<VideoEntity>,
    playlists: List<PlaylistEntity>,
    isBackgroundPlayEnabled: Boolean,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val isStudioOpen by androidx.compose.runtime.remember { androidx.compose.runtime.derivedStateOf { viewModel.isCreatorStudioOpen.value } }
    var showNewPlaylistDialog by remember { mutableStateOf(false) }
    var newPlaylistTitle by remember { mutableStateOf("") }
    var newPlaylistDesc by remember { mutableStateOf("") }

    if (viewModel.isCreatorStudioOpen.value) {
        CreatorAnalyticsScreen(
            analytics = viewModel.creatorAnalytics,
            topVideos = allVideos,
            onBack = { viewModel.openCreatorStudio(false) },
            modifier = modifier
        )
        return
    }

    val downloadedVideos = allVideos.filter { it.isDownloaded }
    val historyVideos = allVideos.take(6)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Creator Studio & Analytics CTA Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurfaceElevated)
                .border(1.dp, YouTubeRed, RoundedCornerShape(14.dp))
                .clickable { viewModel.openCreatorStudio(true) }
                .padding(16.dp)
                .testTag("creator_studio_banner")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(YouTubeRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.BarChart, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Creator Studio & Analytics", color = TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("184.5K views • $1,420 est. revenue", color = TextSecondary, fontSize = 12.sp)
                    }
                }
                Icon(Icons.Default.ChevronRight, contentDescription = "Open", tint = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // History Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Recent History", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(historyVideos, key = { it.id }) { video ->
                Column(
                    modifier = Modifier
                        .width(140.dp)
                        .clickable { onVideoClick(video) }
                        .testTag("history_item_${video.id}")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceElevated)
                    ) {
                        AsyncImage(
                            model = video.thumbnailUrl,
                            contentDescription = video.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xCC000000))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(formatDuration(video.durationSeconds), color = Color.White, fontSize = 9.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(video.title, color = TextPrimary, fontSize = 11.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text(video.creatorName, color = TextSecondary, fontSize = 10.sp, maxLines = 1)
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Offline Downloads Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DownloadDone, contentDescription = null, tint = VerifiedBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Offline Viewing (${downloadedVideos.size})", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (downloadedVideos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceCard)
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No videos downloaded yet. Tap download on any video for offline play.", color = TextSecondary, fontSize = 12.sp)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                downloadedVideos.forEach { vid ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceCard)
                            .clickable { onVideoClick(vid) }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = vid.thumbnailUrl,
                            contentDescription = vid.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(width = 80.dp, height = 48.dp)
                                .clip(RoundedCornerShape(6.dp))
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(vid.title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium, maxLines = 1)
                            Text("${vid.creatorName} • Available offline", color = VerifiedBlue, fontSize = 10.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Custom Playlists Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PlaylistPlay, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Playlists", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            TextButton(
                onClick = { showNewPlaylistDialog = true },
                modifier = Modifier.testTag("create_new_playlist_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = YouTubeRed, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Playlist", color = YouTubeRed, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            playlists.forEach { pl ->
                val videoCount = if (pl.videoIds.isBlank()) 0 else pl.videoIds.split(",").size
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(DarkSurfaceCard)
                        .clickable { /* open playlist */ }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.PlaylistPlay, contentDescription = null, tint = YouTubeRed, modifier = Modifier.size(28.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(pl.title, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text("$videoCount videos • ${if (pl.isPrivate) "Private" else "Public"}", color = TextSecondary, fontSize = 11.sp)
                    }
                    if (pl.isPrivate) {
                        Icon(Icons.Default.Lock, contentDescription = "Private", tint = Color.Gray, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Advanced Settings & Low-End Optimization Toggles
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceCard)
                .padding(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Preferences & Performance", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TextPrimary)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Background Playback", color = TextPrimary, fontSize = 13.sp)
                        Text("Continue playing audio when phone screen is locked or in other apps", color = TextSecondary, fontSize = 10.sp)
                    }
                    Switch(
                        checked = isBackgroundPlayEnabled,
                        onCheckedChange = { viewModel.toggleBackgroundPlay() },
                        colors = SwitchDefaults.colors(checkedThumbColor = YouTubeRed)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Low-End Device Adaptive Mode", color = TextPrimary, fontSize = 13.sp)
                        Text("Aggressive hardware buffer management & memory optimization", color = TextSecondary, fontSize = 10.sp)
                    }
                    Switch(
                        checked = true,
                        onCheckedChange = {},
                        colors = SwitchDefaults.colors(checkedThumbColor = YouTubeRed)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }

    // New Playlist Modal Dialog
    if (showNewPlaylistDialog) {
        AlertDialog(
            onDismissRequest = { showNewPlaylistDialog = false },
            containerColor = DarkSurfaceElevated,
            title = { Text("New Playlist", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newPlaylistTitle,
                        onValueChange = { newPlaylistTitle = it },
                        placeholder = { Text("Playlist Title") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = YouTubeRed
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("new_playlist_title_input")
                    )
                    OutlinedTextField(
                        value = newPlaylistDesc,
                        onValueChange = { newPlaylistDesc = it },
                        placeholder = { Text("Description (optional)") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = YouTubeRed
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("new_playlist_desc_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newPlaylistTitle.isNotBlank()) {
                            viewModel.createPlaylist(newPlaylistTitle, newPlaylistDesc, null)
                            newPlaylistTitle = ""
                            newPlaylistDesc = ""
                            showNewPlaylistDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed)
                ) {
                    Text("Create", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNewPlaylistDialog = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }
}
