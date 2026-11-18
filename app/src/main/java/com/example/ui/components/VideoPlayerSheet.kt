package com.example.ui.components

import android.widget.VideoView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPicture
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.example.data.model.CommentEntity
import com.example.data.model.SubscriptionEntity
import com.example.data.model.VideoEntity
import com.example.service.PlaybackState
import com.example.ui.theme.DarkSurfaceBorder
import com.example.ui.theme.DarkSurfaceCard
import com.example.ui.theme.DarkSurfaceElevated
import com.example.ui.theme.OledBlack
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.VerifiedBlue
import com.example.ui.theme.YouTubeRed
import kotlinx.coroutines.delay

data class TranslationLanguage(
    val code: String,
    val name: String,
    val nativeName: String,
    val flagEmoji: String
)

val SupportedTranslationLanguages = listOf(
    TranslationLanguage("en", "English", "Original", "🇺🇸"),
    TranslationLanguage("es", "Spanish", "Español", "🇪🇸"),
    TranslationLanguage("ja", "Japanese", "日本語", "🇯🇵"),
    TranslationLanguage("fr", "French", "Français", "🇫🇷"),
    TranslationLanguage("de", "German", "Deutsch", "🇩🇪"),
    TranslationLanguage("hi", "Hindi", "हिन्दी", "🇮🇳"),
    TranslationLanguage("zh", "Chinese", "中文", "🇨🇳")
)

fun getInVideoSubtitle(video: VideoEntity, currentSec: Int, langCode: String): String {
    return when (langCode) {
        "es" -> when {
            currentSec < 10 -> "Bienvenidos a \"${video.title}\" con ${video.creatorName}."
            currentSec < 25 -> "En este segmento analizamos los aspectos técnicos clave y el rendimiento."
            currentSec < 40 -> "Observen la precisión de respuesta y la claridad visual sin distorsión."
            currentSec < 60 -> "La arquitectura optimizada permite un bajo consumo de memoria y batería."
            else -> "Gracias por sintonizar. Dale like y suscríbete para la próxima entrega."
        }
        "ja" -> when {
            currentSec < 10 -> "「${video.title}」へようこそ。クリエイター：${video.creatorName}。"
            currentSec < 25 -> "このセクションでは、主要なパフォーマンスと最適化を詳細に検証します。"
            currentSec < 40 -> "リアルタイムな高フレームレートと鮮明なディスプレイ品質をご覧ください。"
            currentSec < 60 -> "低スペック端末でも極めてスムーズに動作する軽量設計が特徴です。"
            else -> "ご視聴ありがとうございました。チャンネル登録をお忘れなく！"
        }
        "fr" -> when {
            currentSec < 10 -> "Bienvenue sur \"${video.title}\" présenté par ${video.creatorName}."
            currentSec < 25 -> "Dans cette section, nous explorons les performances clés et les fonctionnalités."
            currentSec < 40 -> "Remarquez la réactivité fluide et l'optimisation des contrastes."
            currentSec < 60 -> "Une architecture conçue pour une efficacité maximale et une faible consommation."
            else -> "Merci d'avoir regardé. N'oubliez pas d'aimer et de vous abonner !"
        }
        "de" -> when {
            currentSec < 10 -> "Willkommen zu \"${video.title}\" von ${video.creatorName}."
            currentSec < 25 -> "Hier analysieren wir die Kernleistung und wichtige technische Details."
            currentSec < 40 -> "Beachten Sie die flüssige Reaktionszeit und die gestochen scharfe Darstellung."
            currentSec < 60 -> "Das System läuft selbst auf leistungsschwachen Geräten absolut stabil."
            else -> "Vielen Dank fürs Zuschauen. Bitte Kanal abonnieren und Daumen hoch!"
        }
        "hi" -> when {
            currentSec < 10 -> "\"${video.title}\" में आपका स्वागत है। प्रस्तुतकर्ता: ${video.creatorName}।"
            currentSec < 25 -> "इस खंड में हम प्रदर्शन और मुख्य विशेषताओं का विश्लेषण करेंगे।"
            currentSec < 40 -> "देखिए वास्तविक समय में उच्च स्पष्टता और त्वरित प्रतिक्रिया।"
            currentSec < 60 -> "यह आर्किटेक्चर कम बैटरी और रैम खपत के लिए पूरी तरह अनुकूलित है।"
            else -> "देखने के लिए धन्यवाद। कृपया लाइक करें और चैनल को सब्सक्राइब करें!"
        }
        "zh" -> when {
            currentSec < 10 -> "欢迎观看《${video.title}》，创作者：${video.creatorName}。"
            currentSec < 25 -> "在本段落中，我们重点测试核心性能与系统响应速度。"
            currentSec < 40 -> "请看实时渲染带来的高对比度与丝滑流畅度表现。"
            currentSec < 60 -> "优化后的本地架构即使在低端设备上也能极速运行。"
            else -> "感谢观看，欢迎点赞并订阅频道获取最新视频！"
        }
        else -> when {
            currentSec < 10 -> "Welcome to \"${video.title}\" by ${video.creatorName}."
            currentSec < 25 -> "In this section, we analyze the core performance and technical details."
            currentSec < 40 -> "Notice the fluid responsiveness and crisp high-contrast visual display."
            currentSec < 60 -> "Optimized lightweight architecture ensures low battery and memory usage."
            else -> "Thanks for watching! Make sure to like, comment, and subscribe."
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerSheet(
    video: VideoEntity,
    playbackState: PlaybackState,
    isBackgroundPlayEnabled: Boolean,
    comments: List<CommentEntity>,
    channel: SubscriptionEntity?,
    onCollapse: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    onSeekTo: (Int) -> Unit,
    onToggleBackgroundPlay: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit,
    onToggleDownload: () -> Unit,
    onShare: () -> Unit,
    onSaveToPlaylist: () -> Unit,
    onToggleSubscription: () -> Unit,
    onToggleBell: () -> Unit,
    onAddComment: (String) -> Unit,
    onToggleCommentLike: (CommentEntity) -> Unit,
    onEnterSystemPip: () -> Unit,
    onToggleFloatingPip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) }
    var showSettingsMenu by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }
    var showTranslatorDialog by remember { mutableStateOf(false) }
    var isTranslatorEnabled by remember { mutableStateOf(false) }
    var selectedLangCode by remember { mutableStateOf("es") }
    var selectedQuality by remember { mutableStateOf("1080p") }
    var selectedSpeed by remember { mutableFloatStateOf(1.0f) }
    var newCommentText by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    val currentSec = (playbackState.currentPositionMs / 1000).coerceAtLeast(0)
    val totalSec = video.durationSeconds.coerceAtLeast(1)

    // Auto-hide controls after 4 seconds of inactivity while playing
    LaunchedEffect(showControls, playbackState.isPlaying) {
        if (showControls && playbackState.isPlaying) {
            delay(4000)
            showControls = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(OledBlack)
    ) {
        // Video Player Container
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { showControls = !showControls },
                        onDoubleTap = { offset ->
                            if (offset.x < size.width / 2) {
                                onSeekBackward()
                            } else {
                                onSeekForward()
                            }
                        }
                    )
                }
                .testTag("video_player_viewport")
        ) {
            // Native Video Surface
            AndroidView(
                factory = { ctx ->
                    VideoView(ctx).apply {
                        setVideoPath(video.mediaUrl)
                        setOnPreparedListener { mp ->
                            mp.isLooping = true
                            start()
                        }
                    }
                },
                update = { videoView ->
                    if (playbackState.isPlaying) {
                        if (!videoView.isPlaying) videoView.start()
                    } else {
                        if (videoView.isPlaying) videoView.pause()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Buffering Indicator
            if (playbackState.isBuffering) {
                CircularProgressIndicator(
                    color = YouTubeRed,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // In-Video Live Subtitle / Translation Overlay Layer
            if (isTranslatorEnabled) {
                val subtitleText = getInVideoSubtitle(video, currentSec, selectedLangCode)
                val currentLangObj = SupportedTranslationLanguages.find { it.code == selectedLangCode }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (showControls) 54.dp else 14.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xD0000000))
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                        .clickable { showTranslatorDialog = true }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("in_video_translator_overlay")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(YouTubeRed)
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${currentLangObj?.flagEmoji ?: "🌐"} ${selectedLangCode.uppercase()}",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = subtitleText,
                            color = Color(0xFFFFF1A8),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Controls Overlay
            androidx.compose.animation.AnimatedVisibility(
                visible = showControls,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x88000000))
                ) {
                    // Top Bar (Collapse button, Title, In-Video Translator, Background Play, Settings)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onCollapse, modifier = Modifier.testTag("collapse_player_button")) {
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Minimize", tint = Color.White)
                        }

                        Text(
                            text = video.title,
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // In-Video Translator Button with Language Badge
                            IconButton(
                                onClick = { showTranslatorDialog = true },
                                modifier = Modifier.testTag("player_translator_button")
                            ) {
                                BadgedBox(
                                    badge = {
                                        if (isTranslatorEnabled) {
                                            Badge(
                                                containerColor = YouTubeRed,
                                                contentColor = Color.White
                                            ) {
                                                Text(selectedLangCode.uppercase(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Translate,
                                        contentDescription = "In-Video Translator",
                                        tint = if (isTranslatorEnabled) YouTubeRed else Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            // Picture-in-Picture (PiP) Button
                            IconButton(
                                onClick = onEnterSystemPip,
                                modifier = Modifier.testTag("player_pip_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PictureInPictureAlt,
                                    contentDescription = "Picture-in-Picture",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            // Dedicated Background Playback Mode Toggle Button
                            IconButton(
                                onClick = onToggleBackgroundPlay,
                                modifier = Modifier.testTag("player_toggle_background_play_button")
                            ) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = if (isBackgroundPlayEnabled) Color(0xFF2BA640) else Color.DarkGray,
                                            contentColor = Color.White
                                        ) {
                                            Text(if (isBackgroundPlayEnabled) "ON" else "OFF", fontSize = 8.sp)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Headphones,
                                        contentDescription = "Toggle Background Playback",
                                        tint = if (isBackgroundPlayEnabled) Color(0xFF2BA640) else Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            // Settings Cog Dropdown
                            Box {
                                IconButton(onClick = { showSettingsMenu = true }, modifier = Modifier.testTag("player_settings_button")) {
                                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                                }

                                DropdownMenu(
                                    expanded = showSettingsMenu,
                                    onDismissRequest = { showSettingsMenu = false },
                                    modifier = Modifier.background(DarkSurfaceElevated)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Quality: $selectedQuality", color = TextPrimary) },
                                        onClick = {
                                            selectedQuality = if (selectedQuality == "1080p") "720p" else "1080p"
                                            showSettingsMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Speed: ${selectedSpeed}x", color = TextPrimary) },
                                        onClick = {
                                            selectedSpeed = if (selectedSpeed == 1.0f) 1.5f else if (selectedSpeed == 1.5f) 2.0f else 1.0f
                                            showSettingsMenu = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Background Playback", color = TextPrimary)
                                                Switch(
                                                    checked = isBackgroundPlayEnabled,
                                                    onCheckedChange = { onToggleBackgroundPlay() },
                                                    colors = SwitchDefaults.colors(checkedThumbColor = YouTubeRed)
                                                )
                                            }
                                        },
                                        onClick = { onToggleBackgroundPlay() }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text("Live Translator Subtitles", color = TextPrimary)
                                                Switch(
                                                    checked = isTranslatorEnabled,
                                                    onCheckedChange = { isTranslatorEnabled = it },
                                                    colors = SwitchDefaults.colors(checkedThumbColor = YouTubeRed)
                                                )
                                            }
                                        },
                                        onClick = { isTranslatorEnabled = !isTranslatorEnabled }
                                    )
                                }
                            }
                        }
                    }

                    // Center Playback Controls (10s Rewind, Play/Pause, 10s Forward)
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalArrangement = Arrangement.spacedBy(28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 10s Rewind Seek Button
                        IconButton(
                            onClick = onSeekBackward,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0x66000000))
                                .testTag("seek_back_10_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Seek back 10 seconds",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        // Play / Pause Button
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xCC000000))
                                .border(1.5.dp, Color.White, CircleShape)
                                .clickable(onClick = onTogglePlayPause)
                                .testTag("player_play_pause_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (playbackState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (playbackState.isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        // 10s Fast-Forward Seek Button
                        IconButton(
                            onClick = onSeekForward,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0x66000000))
                                .testTag("seek_forward_10_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Seek forward 10 seconds",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }

                    // Bottom Seekbar & Quick Control Pills
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        val progressFraction = (currentSec.toFloat() / totalSec.toFloat()).coerceIn(0f, 1f)

                        Slider(
                            value = progressFraction,
                            onValueChange = { frac ->
                                onSeekTo((frac * totalSec * 1000).toInt())
                            },
                            colors = SliderDefaults.colors(
                                thumbColor = YouTubeRed,
                                activeTrackColor = YouTubeRed,
                                inactiveTrackColor = Color(0x66FFFFFF)
                            ),
                            modifier = Modifier.height(20.dp).testTag("video_progress_slider")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${formatDuration(currentSec)} / ${formatDuration(totalSec)}",
                                color = Color.White,
                                fontSize = 11.sp
                            )

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // In-Video Translator Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isTranslatorEnabled) YouTubeRed else Color(0x66000000))
                                        .clickable { showTranslatorDialog = true }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                        .testTag("quick_translator_pill")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Translate,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isTranslatorEnabled) "Translate: ${selectedLangCode.uppercase()}" else "Translator",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Quick Background Playback Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isBackgroundPlayEnabled) Color(0xFF2BA640) else Color(0x66000000))
                                        .clickable { onToggleBackgroundPlay() }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                        .testTag("quick_bg_play_pill")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Headphones,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isBackgroundPlayEnabled) "BG: ON" else "BG: OFF",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                // Floating In-App PiP Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0x66000000))
                                        .clickable { onToggleFloatingPip() }
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                        .testTag("quick_floating_pip_pill")
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.PictureInPictureAlt,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Floating PiP",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Text(
                                    text = selectedQuality,
                                    color = Color.Gray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Scrollable Video Details & Actions
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(14.dp)
        ) {
            // Title
            Text(
                text = video.title,
                color = TextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Views & Time
            Text(
                text = "${formatViews(video.viewsCount)} • ${formatRelativeTime(video.uploadTimestamp)} • #${video.category}",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Action Buttons Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Like / Dislike Pill Group
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(DarkSurfaceElevated),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onToggleLike)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("action_like_button"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (video.isLiked) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                            contentDescription = "Like",
                            tint = if (video.isLiked) YouTubeRed else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatViews(video.likesCount).replace(" views", ""),
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(18.dp)
                            .background(DarkSurfaceBorder)
                    )

                    IconButton(
                        onClick = onToggleDislike,
                        modifier = Modifier.size(36.dp).testTag("action_dislike_button")
                    ) {
                        Icon(
                            imageVector = if (video.isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDown,
                            contentDescription = "Dislike",
                            tint = if (video.isDisliked) YouTubeRed else TextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Share Button
                ActionButton(
                    icon = Icons.Default.Share,
                    label = "Share",
                    onClick = onShare,
                    testTag = "action_share_button"
                )

                // Download Button (Offline caching)
                ActionButton(
                    icon = if (video.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                    label = if (video.isDownloaded) "Downloaded" else "Download",
                    isActive = video.isDownloaded,
                    onClick = onToggleDownload,
                    testTag = "action_download_button"
                )

                // Save to Playlist Button
                ActionButton(
                    icon = Icons.Outlined.PlaylistAdd,
                    label = "Save",
                    onClick = onSaveToPlaylist,
                    testTag = "action_save_playlist_button"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = DarkSurfaceBorder)
            Spacer(modifier = Modifier.height(14.dp))

            // Channel Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = video.creatorAvatarUrl,
                        contentDescription = video.creatorName,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DarkSurfaceElevated)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = video.creatorName,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = channel?.subscriberCount ?: "1.4M subscribers",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isSubscribed = channel?.isSubscribed ?: true
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSubscribed) DarkSurfaceElevated else Color.White)
                            .clickable(onClick = onToggleSubscription)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("channel_subscribe_button")
                    ) {
                        Text(
                            text = if (isSubscribed) "Subscribed" else "Subscribe",
                            color = if (isSubscribed) TextPrimary else Color.Black,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (isSubscribed) {
                        IconButton(
                            onClick = onToggleBell,
                            modifier = Modifier.size(36.dp).padding(start = 4.dp).testTag("channel_bell_button")
                        ) {
                            Icon(
                                imageVector = if (channel?.hasNotificationBell == true) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                                contentDescription = "Bell",
                                tint = if (channel?.hasNotificationBell == true) YouTubeRed else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceCard)
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Description",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = video.description,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Comments Preview Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DarkSurfaceCard)
                    .clickable { showCommentsSheet = true }
                    .padding(12.dp)
                    .testTag("comments_preview_card")
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Comments • ${comments.size}",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "View all",
                            color = VerifiedBlue,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (comments.isNotEmpty()) {
                        val firstComment = comments.first()
                        Row(verticalAlignment = Alignment.Top) {
                            AsyncImage(
                                model = firstComment.authorAvatarUrl,
                                contentDescription = firstComment.authorName,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(DarkSurfaceElevated)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = firstComment.authorName,
                                    color = TextSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = firstComment.content,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Be the first to comment!",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Full Comments Bottom Sheet
        if (showCommentsSheet) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { showCommentsSheet = false },
                sheetState = sheetState,
                containerColor = DarkSurfaceElevated
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Comments (${comments.size})",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Add Comment Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newCommentText,
                            onValueChange = { newCommentText = it },
                            placeholder = { Text("Add a comment...", color = Color.Gray, fontSize = 13.sp) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                focusedBorderColor = YouTubeRed,
                                unfocusedBorderColor = DarkSurfaceBorder
                            ),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.weight(1f).testTag("new_comment_input")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (newCommentText.isNotBlank()) {
                                    onAddComment(newCommentText)
                                    newCommentText = ""
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(YouTubeRed)
                                .testTag("post_comment_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Post", tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Comments List
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        comments.forEach { comment ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.Top
                            ) {
                                AsyncImage(
                                    model = comment.authorAvatarUrl,
                                    contentDescription = comment.authorName,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(DarkSurfaceBorder)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(comment.authorName, color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(formatRelativeTime(comment.timestamp), color = Color.Gray, fontSize = 10.sp)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(comment.content, color = TextPrimary, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        IconButton(
                                            onClick = { onToggleCommentLike(comment) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (comment.isLikedByMe) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUp,
                                                contentDescription = "Like",
                                                tint = if (comment.isLikedByMe) YouTubeRed else Color.Gray,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                        Text("${comment.likesCount}", color = Color.Gray, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // In-Video Live Translator Options Modal Dialog
        if (showTranslatorDialog) {
            AlertDialog(
                onDismissRequest = { showTranslatorDialog = false },
                containerColor = DarkSurfaceElevated,
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = YouTubeRed, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("In-Video Live Translator", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Translation On/Off Switch Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(DarkSurfaceCard)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Live Subtitles Overlay", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                Text("Translate speech in real time", color = TextSecondary, fontSize = 11.sp)
                            }
                            Switch(
                                checked = isTranslatorEnabled,
                                onCheckedChange = { isTranslatorEnabled = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = YouTubeRed)
                            )
                        }

                        Text("Select Target Translation Language:", color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)

                        // Languages List
                        SupportedTranslationLanguages.forEach { lang ->
                            val isSelected = lang.code == selectedLangCode
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) YouTubeRed.copy(alpha = 0.2f) else DarkSurfaceCard)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) YouTubeRed else DarkSurfaceBorder,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        selectedLangCode = lang.code
                                        isTranslatorEnabled = true
                                    }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(lang.flagEmoji, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(lang.name, color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                        Text(lang.nativeName, color = Color.Gray, fontSize = 11.sp)
                                    }
                                }

                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = "Selected", tint = YouTubeRed, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showTranslatorDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = YouTubeRed)
                    ) {
                        Text("Done", color = Color.White)
                    }
                }
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurfaceElevated)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isActive) VerifiedBlue else TextPrimary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                color = if (isActive) VerifiedBlue else TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
