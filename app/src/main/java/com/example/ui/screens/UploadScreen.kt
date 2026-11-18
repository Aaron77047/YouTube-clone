package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.YouTubeRed
import com.example.viewmodel.YouTubeViewModel

@Composable
fun UploadScreen(
    viewModel: YouTubeViewModel,
    modifier: Modifier = Modifier
) {
    var isMusicUpload by remember { mutableStateOf(false) }
    var selectedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tech") }
    var tags by remember { mutableStateOf("") }
    var isPublishing by remember { mutableStateOf(false) }
    var showCategoryMenu by remember { mutableStateOf(false) }

    val categories = listOf("Tech", "Music", "Gaming", "Science", "Coding", "Lifestyle", "Podcasts")

    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedMediaUri = uri
            selectedFileName = uri.lastPathSegment ?: if (isMusicUpload) "audio_track.mp3" else "video_stream.mp4"
            if (title.isBlank()) {
                title = selectedFileName.substringBeforeLast(".").replace("_", " ").capitalize()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Creator Upload Studio",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Publish videos or high-fidelity music tracks to your channel",
            fontSize = 12.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Type Selector (Video vs Music)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceElevated)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isMusicUpload) YouTubeRed else Color.Transparent)
                    .clickable {
                        isMusicUpload = false
                        selectedCategory = "Tech"
                    }
                    .padding(vertical = 10.dp)
                    .testTag("upload_type_video"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Video Upload", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isMusicUpload) YouTubeRed else Color.Transparent)
                    .clickable {
                        isMusicUpload = true
                        selectedCategory = "Music"
                    }
                    .padding(vertical = 10.dp)
                    .testTag("upload_type_music"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Audiotrack, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Music Upload", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // File Selector Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkSurfaceCard)
                .border(1.dp, if (selectedMediaUri != null) YouTubeRed else DarkSurfaceBorder, RoundedCornerShape(12.dp))
                .clickable {
                    if (isMusicUpload) {
                        mediaPickerLauncher.launch("audio/*")
                    } else {
                        mediaPickerLauncher.launch("video/*")
                    }
                }
                .testTag("media_file_picker_box"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = if (selectedMediaUri != null) Icons.Default.UploadFile else Icons.Default.CloudUpload,
                    contentDescription = null,
                    tint = if (selectedMediaUri != null) YouTubeRed else TextSecondary,
                    modifier = Modifier.size(38.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (selectedMediaUri != null) "Selected: $selectedFileName" else if (isMusicUpload) "Tap to select Audio track (MP3/WAV)" else "Tap to select Video file (MP4/MKV)",
                    color = if (selectedMediaUri != null) TextPrimary else Color.Gray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                if (selectedMediaUri == null) {
                    Text(
                        text = "Or publish sample demo instantly",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Title Input
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title (required)") },
            placeholder = { Text(if (isMusicUpload) "e.g. Neon Sunset - Synthwave Mix" else "e.g. My New Tech Review 2026") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = YouTubeRed,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedLabelColor = YouTubeRed,
                unfocusedLabelColor = Color.Gray
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("upload_title_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Tell viewers about your media...") },
            minLines = 3,
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = YouTubeRed,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedLabelColor = YouTubeRed,
                unfocusedLabelColor = Color.Gray
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("upload_description_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Category Selector Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    Text("▼", color = Color.Gray, modifier = Modifier.padding(end = 12.dp))
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = YouTubeRed,
                    unfocusedBorderColor = DarkSurfaceBorder,
                    focusedLabelColor = YouTubeRed,
                    unfocusedLabelColor = Color.Gray
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCategoryMenu = true }
                    .testTag("upload_category_field")
            )

            DropdownMenu(
                expanded = showCategoryMenu,
                onDismissRequest = { showCategoryMenu = false },
                modifier = Modifier.background(DarkSurfaceElevated)
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat, color = TextPrimary) },
                        onClick = {
                            selectedCategory = cat
                            showCategoryMenu = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Tags Input
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Tags (comma separated)") },
            placeholder = { Text("e.g. tech, music, review, viral") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = YouTubeRed,
                unfocusedBorderColor = DarkSurfaceBorder,
                focusedLabelColor = YouTubeRed,
                unfocusedLabelColor = Color.Gray
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("upload_tags_input")
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Submit Button
        Button(
            onClick = {
                isPublishing = true
                viewModel.uploadMedia(
                    title = title,
                    description = description,
                    category = selectedCategory,
                    tags = tags,
                    isMusic = isMusicUpload,
                    mediaUri = selectedMediaUri
                )
            },
            colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed),
            shape = RoundedCornerShape(24.dp),
            enabled = !isPublishing,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("publish_media_button")
        ) {
            if (isPublishing) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = if (isMusicUpload) "Publish Music Track" else "Publish Video",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(70.dp))
    }
}
