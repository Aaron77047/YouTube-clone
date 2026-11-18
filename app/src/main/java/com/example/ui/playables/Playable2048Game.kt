package com.example.ui.playables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.YouTubeRed
import kotlin.math.abs
import kotlin.random.Random

enum class Direction { UP, DOWN, LEFT, RIGHT }

@Composable
fun Playable2048Game(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var board by remember { mutableStateOf(initBoard()) }
    var score by remember { mutableIntStateOf(0) }
    var bestScore by remember { mutableIntStateOf(2048) }
    var isGameOver by remember { mutableStateOf(false) }
    var hasWon by remember { mutableStateOf(false) }

    fun move(direction: Direction) {
        if (isGameOver) return
        val (newBoard, earnedPoints, moved) = processMove(board, direction)
        if (moved) {
            board = spawnRandomTile(newBoard)
            score += earnedPoints
            if (score > bestScore) bestScore = score
            if (board.any { row -> row.any { it >= 2048 } }) {
                hasWon = true
            }
            if (checkGameOver(board)) {
                isGameOver = true
            }
        }
    }

    fun restart() {
        board = initBoard()
        score = 0
        isGameOver = false
        hasWon = false
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
            IconButton(
                onClick = onClose,
                modifier = Modifier.testTag("close_game_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Exit Game",
                    tint = TextPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "2048 Playable",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = YouTubeRed
                )
                Text(
                    text = "Swipe or tap arrows to merge",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { restart() },
                modifier = Modifier.testTag("restart_game_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Restart",
                    tint = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Score row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ScoreBadge(label = "SCORE", value = score)
            ScoreBadge(label = "BEST", value = bestScore)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Board Container with Gesture Support
        var totalDragX by remember { mutableStateOf(0f) }
        var totalDragY by remember { mutableStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkSurfaceElevated)
                .border(2.dp, Color(0xFF383838), RoundedCornerShape(16.dp))
                .padding(8.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            if (abs(totalDragX) > abs(totalDragY)) {
                                if (totalDragX > 40) move(Direction.RIGHT)
                                else if (totalDragX < -40) move(Direction.LEFT)
                            } else {
                                if (totalDragY > 40) move(Direction.DOWN)
                                else if (totalDragY < -40) move(Direction.UP)
                            }
                            totalDragX = 0f
                            totalDragY = 0f
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            totalDragX += dragAmount.x
                            totalDragY += dragAmount.y
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (r in 0..3) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (c in 0..3) {
                            val tileValue = board[r][c]
                            TileView(
                                value = tileValue,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxSize()
                            )
                        }
                    }
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
                            colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed)
                        ) {
                            Text("Try Again", color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // On-screen D-Pad for accessible, precise controls
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(
                onClick = { move(Direction.UP) },
                modifier = Modifier
                    .size(48.dp)
                    .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                    .testTag("dpad_up")
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = TextPrimary)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { move(Direction.LEFT) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                        .testTag("dpad_left")
                ) {
                    Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left", tint = TextPrimary)
                }
                IconButton(
                    onClick = { move(Direction.DOWN) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                        .testTag("dpad_down")
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = TextPrimary)
                }
                IconButton(
                    onClick = { move(Direction.RIGHT) },
                    modifier = Modifier
                        .size(48.dp)
                        .background(DarkSurfaceCard, RoundedCornerShape(24.dp))
                        .testTag("dpad_right")
                ) {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Right", tint = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun ScoreBadge(label: String, value: Int) {
    Column(
        modifier = Modifier
            .width(110.dp)
            .background(DarkSurfaceCard, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFF383838), RoundedCornerShape(12.dp))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
fun TileView(value: Int, modifier: Modifier = Modifier) {
    val scale by animateFloatAsState(targetValue = if (value > 0) 1f else 0.9f, label = "tileScale")
    val bgColor = getTileColor(value)
    val textColor = if (value in listOf(2, 4)) Color(0xFF222222) else Color.White

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        if (value > 0) {
            Text(
                text = value.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = if (value >= 1024) 18.sp else if (value >= 128) 22.sp else 26.sp,
                color = textColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun getTileColor(value: Int): Color {
    return when (value) {
        0 -> Color(0xFF1E1E1E)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> YouTubeRed
    }
}

fun initBoard(): List<List<Int>> {
    var board = List(4) { List(4) { 0 } }
    board = spawnRandomTile(board)
    board = spawnRandomTile(board)
    return board
}

fun spawnRandomTile(board: List<List<Int>>): List<List<Int>> {
    val emptySpots = mutableListOf<Pair<Int, Int>>()
    for (r in 0..3) {
        for (c in 0..3) {
            if (board[r][c] == 0) emptySpots.add(Pair(r, c))
        }
    }
    if (emptySpots.isEmpty()) return board
    val (r, c) = emptySpots.random()
    val value = if (Random.nextFloat() < 0.9f) 2 else 4
    val mutable = board.map { it.toMutableList() }.toMutableList()
    mutable[r][c] = value
    return mutable
}

fun processMove(board: List<List<Int>>, direction: Direction): Triple<List<List<Int>>, Int, Boolean> {
    var points = 0
    var moved = false
    val mutable = board.map { it.toMutableList() }.toMutableList()

    fun slideRow(row: List<Int>): Pair<List<Int>, Int> {
        val filtered = row.filter { it != 0 }
        val result = mutableListOf<Int>()
        var i = 0
        var scoreGain = 0
        while (i < filtered.size) {
            if (i + 1 < filtered.size && filtered[i] == filtered[i + 1]) {
                val merged = filtered[i] * 2
                result.add(merged)
                scoreGain += merged
                i += 2
            } else {
                result.add(filtered[i])
                i++
            }
        }
        while (result.size < 4) {
            result.add(0)
        }
        return Pair(result, scoreGain)
    }

    when (direction) {
        Direction.LEFT -> {
            for (r in 0..3) {
                val (newRow, gain) = slideRow(mutable[r])
                points += gain
                if (newRow != mutable[r]) moved = true
                mutable[r] = newRow.toMutableList()
            }
        }
        Direction.RIGHT -> {
            for (r in 0..3) {
                val (newRow, gain) = slideRow(mutable[r].reversed())
                points += gain
                val reversedBack = newRow.reversed()
                if (reversedBack != mutable[r]) moved = true
                mutable[r] = reversedBack.toMutableList()
            }
        }
        Direction.UP -> {
            for (c in 0..3) {
                val col = listOf(mutable[0][c], mutable[1][c], mutable[2][c], mutable[3][c])
                val (newCol, gain) = slideRow(col)
                points += gain
                for (r in 0..3) {
                    if (mutable[r][c] != newCol[r]) moved = true
                    mutable[r][c] = newCol[r]
                }
            }
        }
        Direction.DOWN -> {
            for (c in 0..3) {
                val col = listOf(mutable[3][c], mutable[2][c], mutable[1][c], mutable[0][c])
                val (newCol, gain) = slideRow(col)
                points += gain
                val revBack = newCol.reversed()
                for (r in 0..3) {
                    if (mutable[r][c] != revBack[r]) moved = true
                    mutable[r][c] = revBack[r]
                }
            }
        }
    }

    return Triple(mutable, points, moved)
}

fun checkGameOver(board: List<List<Int>>): Boolean {
    for (r in 0..3) {
        for (c in 0..3) {
            if (board[r][c] == 0) return false
            if (c + 1 < 4 && board[r][c] == board[r][c + 1]) return false
            if (r + 1 < 4 && board[r][c] == board[r + 1][c]) return false
        }
    }
    return true
}
