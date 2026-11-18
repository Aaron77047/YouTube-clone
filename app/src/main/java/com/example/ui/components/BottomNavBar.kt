package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.YouTubeRed
import com.example.viewmodel.MainTab

@Composable
fun YouTubeBottomNavBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(OledBlack)
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(DarkSurfaceBorder)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                isSelected = currentTab == MainTab.HOME,
                onClick = { onTabSelected(MainTab.HOME) },
                testTag = "nav_tab_home"
            )

            NavItem(
                label = "Playables",
                selectedIcon = Icons.Filled.SportsEsports,
                unselectedIcon = Icons.Outlined.SportsEsports,
                isSelected = currentTab == MainTab.PLAYABLES,
                onClick = { onTabSelected(MainTab.PLAYABLES) },
                testTag = "nav_tab_playables"
            )

            // Centered Upload Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onTabSelected(MainTab.UPLOAD) }
                    .testTag("nav_tab_upload"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Upload",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            NavItem(
                label = "Subscriptions",
                selectedIcon = Icons.Filled.Subscriptions,
                unselectedIcon = Icons.Outlined.Subscriptions,
                isSelected = currentTab == MainTab.SUBSCRIPTIONS,
                onClick = { onTabSelected(MainTab.SUBSCRIPTIONS) },
                testTag = "nav_tab_subscriptions"
            )

            NavItem(
                label = "Library",
                selectedIcon = Icons.Filled.VideoLibrary,
                unselectedIcon = Icons.Outlined.VideoLibrary,
                isSelected = currentTab == MainTab.LIBRARY,
                onClick = { onTabSelected(MainTab.LIBRARY) },
                testTag = "nav_tab_library"
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 48.dp)
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else unselectedIcon,
                contentDescription = label,
                tint = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}
