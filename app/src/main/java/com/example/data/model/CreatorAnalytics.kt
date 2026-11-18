package com.example.data.model

data class CreatorAnalytics(
    val channelName: String = "Your Creator Studio",
    val totalViews: Long = 184520,
    val totalWatchTimeHours: Float = 4210.5f,
    val subscriberCount: Int = 12450,
    val estimatedRevenue: Double = 1420.80,
    val impressionsClickThroughRate: Float = 8.4f,
    val averageViewDuration: String = "4:32",
    val realTimeViews48Hours: Int = 8940,
    val subscriberGrowthDelta: Int = 340,
    val weeklyViewsData: List<Int> = listOf(14200, 18500, 16400, 21300, 24800, 28900, 31200)
)
