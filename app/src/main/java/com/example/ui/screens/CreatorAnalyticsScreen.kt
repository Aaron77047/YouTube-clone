package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CreatorAnalytics
import com.example.data.model.VideoEntity
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.LikeGreen
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.theme.YouTubeRed

@Composable
fun CreatorAnalyticsScreen(
    analytics: CreatorAnalytics,
    topVideos: List<VideoEntity>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_analytics")) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextPrimary)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Channel Analytics",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Last 28 days performance",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2x2 Key Metric Cards Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AnalyticsStatCard(
                title = "Total Views",
                value = "184.5K",
                delta = "+14.2%",
                icon = Icons.Default.Visibility,
                iconColor = VerifiedBlue,
                modifier = Modifier.weight(1f)
            )
            AnalyticsStatCard(
                title = "Watch Time",
                value = "4,210 hrs",
                delta = "+18.8%",
                icon = Icons.Default.Schedule,
                iconColor = LikeGreen,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AnalyticsStatCard(
                title = "Subscribers",
                value = "12,450",
                delta = "+340",
                icon = Icons.Default.Group,
                iconColor = YouTubeRed,
                modifier = Modifier.weight(1f)
            )
            AnalyticsStatCard(
                title = "Est. Revenue",
                value = "$1,420.80",
                delta = "+22.4%",
                icon = Icons.Default.AttachMoney,
                iconColor = Color(0xFFFFD700),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Weekly Views Trend Bar Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurfaceCard)
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Weekly Views Velocity",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = "Real-time updates",
                        fontSize = 11.sp,
                        color = VerifiedBlue
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pure Compose Canvas Bar Chart
                val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                val views = analytics.weeklyViewsData
                val maxView = views.maxOrNull()?.toFloat() ?: 1f

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val barWidth = size.width / (views.size * 2f)
                        val stepX = size.width / views.size

                        views.forEachIndexed { i, value ->
                            val barHeight = (value / maxView) * (size.height - 25.dp.toPx())
                            val x = i * stepX + (stepX - barWidth) / 2f
                            val y = size.height - barHeight

                            // Gradient red bar
                            drawRoundRect(
                                color = if (i == views.size - 1) Color(0xFFFF0000) else Color(0xFF882222),
                                topLeft = Offset(x, y),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(6f)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    days.forEach { day ->
                        Text(text = day, color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Audience Engagement Metrics
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(DarkSurfaceCard)
                .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Audience Retention & Reach",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Impressions Click-Through (CTR)", color = TextSecondary, fontSize = 13.sp)
                    Text("8.4%", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Average View Duration", color = TextSecondary, fontSize = 13.sp)
                    Text("4:32 min", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Realtime 48-Hour Views", color = TextSecondary, fontSize = 13.sp)
                    Text("8,940", color = LikeGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Top Content Breakdown
        Text(
            text = "Top Performing Content",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(10.dp))

        topVideos.take(3).forEachIndexed { index, video ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(DarkSurfaceElevated)
                    .padding(10.dp)
                    .padding(bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "#${index + 1}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = YouTubeRed,
                    modifier = Modifier.width(28.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = video.title,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                    Text(
                        text = "${video.viewsCount} views • ${video.likesCount} likes",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
private fun AnalyticsStatCard(
    title: String,
    value: String,
    delta: String,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DarkSurfaceCard)
            .border(1.dp, DarkSurfaceBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = TextSecondary, fontSize = 11.sp)
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ArrowDropUp, contentDescription = null, tint = LikeGreen, modifier = Modifier.size(16.dp))
                Text(delta, color = LikeGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
