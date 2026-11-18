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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameType
import com.example.data.model.PlayableGame
import com.example.ui.playables.Playable2048Game
import com.example.ui.playables.PlayableSnakeGame
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.GoldBadge
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.YouTubeRed
import com.example.viewmodel.YouTubeViewModel

@Composable
fun PlayablesScreen(
    viewModel: YouTubeViewModel,
    games: List<PlayableGame>,
    activeGame: PlayableGame?,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf("All") }
    val focusManager = LocalFocusManager.current

    // If an interactive game is active, render the full-screen game experience
    if (activeGame != null) {
        when (activeGame.gameType) {
            GameType.GAME_2048 -> {
                Playable2048Game(
                    onClose = { viewModel.openPlayableGame(null) },
                    modifier = modifier
                )
                return
            }
            GameType.SNAKE -> {
                PlayableSnakeGame(
                    onClose = { viewModel.openPlayableGame(null) },
                    modifier = modifier
                )
                return
            }
            GameType.RETRO_TAP -> {
                Playable2048Game(
                    onClose = { viewModel.openPlayableGame(null) },
                    modifier = modifier
                )
                return
            }
        }
    }

    val genres = listOf("All", "Puzzle", "Arcade", "Action")
    val filteredGames = games.filter { game ->
        (selectedGenre == "All" || game.category.equals(selectedGenre, ignoreCase = true)) &&
                (searchQuery.isBlank() || game.title.contains(searchQuery, ignoreCase = true) || game.subtitle.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
            .padding(horizontal = 14.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Dedicated Search Bar for YouTube Playables
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.setPlayablesSearchQuery(it)
            },
            placeholder = { Text("Search YouTube Playables...", color = Color.Gray, fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.SportsEsports,
                    contentDescription = null,
                    tint = YouTubeRed,
                    modifier = Modifier.size(22.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkSurfaceElevated,
                unfocusedContainerColor = DarkSurfaceElevated,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = YouTubeRed,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(24.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("playables_search_bar")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Playables Genres Filter Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                val isSelected = genre.equals(selectedGenre, ignoreCase = true)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) YouTubeRed else DarkSurfaceCard)
                        .clickable { selectedGenre = genre }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag("playable_filter_$genre")
                ) {
                    Text(
                        text = genre,
                        color = if (isSelected) Color.White else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Games List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Hero Spotlight
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(DarkSurfaceElevated)
                        .border(1.dp, YouTubeRed, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("FEATURED PLAYABLE", color = YouTubeRed, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Instant Gaming, Zero Downloads", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Play seamless casual games directly in your YouTube stream.", color = TextSecondary, fontSize = 12.sp)
                        }
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(YouTubeRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.SportsEsports, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }

            items(filteredGames, key = { it.id }) { game ->
                PlayableCard(
                    game = game,
                    onPlayClick = { viewModel.openPlayableGame(game) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
fun PlayableCard(
    game: PlayableGame,
    onPlayClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(DarkSurfaceCard)
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onPlayClick)
            .padding(14.dp)
            .testTag("playable_card_${game.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Badge
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(game.bannerColorHex)),
                contentAlignment = Alignment.Center
            ) {
                Text(game.iconEmoji, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = game.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(DarkSurfaceElevated)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(game.category, color = Color.Gray, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = game.subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = GoldBadge, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text("${game.rating}", color = GoldBadge, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(game.playersCount, color = Color.Gray, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Play Button
            Button(
                onClick = onPlayClick,
                colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.testTag("play_button_${game.id}")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Play", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
