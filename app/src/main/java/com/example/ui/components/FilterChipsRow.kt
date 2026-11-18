package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChipBackground
import com.example.ui.theme.ChipSelectedBackground
import com.example.ui.theme.ChipSelectedText
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary

@Composable
fun FilterChipsRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("All", "Music", "Gaming", "Tech", "Science", "Coding", "Downloaded")
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(OledBlack)
            .padding(vertical = 8.dp, horizontal = 12.dp)
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            val isSelected = category.equals(selectedCategory, ignoreCase = true)
            Box(
                modifier = Modifier
                    .height(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isSelected) ChipSelectedBackground else ChipBackground)
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 14.dp)
                    .testTag("filter_chip_$category"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category,
                    fontSize = 13.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) ChipSelectedText else TextPrimary
                )
            }
        }
    }
}
