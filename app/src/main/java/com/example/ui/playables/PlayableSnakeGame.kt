package com.example.ui.playables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.YouTubeRed
import kotlinx.coroutines.delay
import kotlin.random.Random

data class SnakePoint(val x: Int, val y: Int)

@Composable
fun PlayableSnakeGame(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gridSize = 18
    var snake by remember { mutableStateOf(listOf(SnakePoint(8, 8), SnakePoint(8, 9), SnakePoint(8, 10))) }
    var direction by remember { mutableStateOf(Direction.UP) }
    var nextDirection by remember { mutableStateOf(Direction.UP) }
    var food by remember { mutableStateOf(SnakePoint(5, 5)) }
    var score by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(14) }
    var isGameOver by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    fun spawnFood(currentSnake: List<SnakePoint>): SnakePoint {
        while (true) {
            val point = SnakePoint(Random.nextInt(gridSize), Random.nextInt(gridSize))
            if (!currentSnake.contains(point)) return point
        }
    }

    fun restart() {
        snake = listOf(SnakePoint(8, 8), SnakePoint(8, 9), SnakePoint(8, 10))
        direction = Direction.UP
        nextDirection = Direction.UP
        score = 0
        isGameOver = false
        food = spawnFood(snake)
    }

    // Game Loop
    LaunchedEffect(isGameOver, isPaused) {
        while (!isGameOver && !isPaused) {
            val tickDelay = (180 - (score * 3).coerceAtMost(100)).toLong()
            delay(tickDelay)

            direction = nextDirection
            val head = snake.first()
            val newHead = when (direction) {
                Direction.UP -> SnakePoint(head.x, (head.y - 1 + gridSize) % gridSize)
                Direction.DOWN -> SnakePoint(head.x, (head.y + 1) % gridSize)
                Direction.LEFT -> SnakePoint((head.x - 1 + gridSize) % gridSize, head.y)
                Direction.RIGHT -> SnakePoint((head.x + 1) % gridSize, head.y)
            }

            if (snake.dropLast(1).contains(newHead)) {
                isGameOver = true
                if (score > bestScore) bestScore = score
            } else {
                if (newHead == food) {
                    snake = listOf(newHead) + snake
                    score += 1
                    food = spawnFood(snake)
                } else {
                    snake = listOf(newHead) + snake.dropLast(1)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onClose, modifier = Modifier.testTag("close_snake_button")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Exit Game", tint = TextPrimary)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Retro Snake",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2BA640)
                )
                Text(
                    text = "Apples eaten: $score",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            IconButton(onClick = { restart() }, modifier = Modifier.testTag("restart_snake_button")) {
                Icon(Icons.Default.Refresh, contentDescription = "Restart", tint = TextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Score cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoreBadge(label = "SCORE", value = score)
            ScoreBadge(label = "RECORD", value = bestScore)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Canvas Game Board
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurfaceElevated)
                .border(2.dp, Color(0xFF2BA640), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                val cellWidth = size.width / gridSize
                val cellHeight = size.height / gridSize

                // Draw Food
                drawCircle(
                    color = Color(0xFFFF0000),
                    radius = (cellWidth / 2.2f),
                    center = Offset(
                        food.x * cellWidth + cellWidth / 2f,
                        food.y * cellHeight + cellHeight / 2f
                    )
                )

                // Draw Snake
                snake.forEachIndexed { index, part ->
                    val isHead = index == 0
                    val snakeColor = if (isHead) Color(0xFF4CAF50) else Color(0xFF2BA640)
                    drawRoundRect(
                        color = snakeColor,
                        topLeft = Offset(part.x * cellWidth + 1f, part.y * cellHeight + 1f),
                        size = Size(cellWidth - 2f, cellHeight - 2f),
                        cornerRadius = CornerRadius(if (isHead) 6f else 3f)
                    )
                }
            }

            if (isGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xDD000000)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Game Over!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = YouTubeRed
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { restart() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2BA640))
                        ) {
                            Text("Play Again", color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // D-pad Controls
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = { if (direction != Direction.DOWN) nextDirection = Direction.UP },
                modifier = Modifier
                    .size(48.dp)
                    .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                    .testTag("snake_dpad_up")
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = TextPrimary)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { if (direction != Direction.RIGHT) nextDirection = Direction.LEFT },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                        .testTag("snake_dpad_left")
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left", tint = TextPrimary)
                }
                IconButton(
                    onClick = { if (direction != Direction.UP) nextDirection = Direction.DOWN },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                        .testTag("snake_dpad_down")
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = TextPrimary)
                }
                IconButton(
                    onClick = { if (direction != Direction.LEFT) nextDirection = Direction.RIGHT },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                        .testTag("snake_dpad_right")
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Right", tint = TextPrimary)
                }
            }
        }
    }
}
