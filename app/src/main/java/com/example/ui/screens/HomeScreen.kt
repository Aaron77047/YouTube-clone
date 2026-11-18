package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VideoEntity
import com.example.ui.components.FilterChipsRow
import com.example.ui.components.VideoCard
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.YouTubeViewModel

@Composable
fun HomeScreen(
    viewModel: YouTubeViewModel,
    videos: List<VideoEntity>,
    selectedCategory: String,
    onVideoClick: (VideoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
    ) {
        FilterChipsRow(
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.selectCategory(it) }
        )

        if (videos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No videos found",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try clearing filters or search for something else",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 60.dp)
            ) {
                items(videos, key = { it.id }) { video ->
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
}
