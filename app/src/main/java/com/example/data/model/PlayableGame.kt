package com.example.data.model

enum class GameType {
    GAME_2048,
    SNAKE,
    RETRO_TAP
}

data class PlayableGame(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String, // Arcade, Puzzle, Action, Strategy
    val rating: Float,
    val playersCount: String,
    val gameType: GameType,
    val bannerColorHex: Long,
    val iconEmoji: String,
    val description: String,
    val isFeatured: Boolean = false
)
