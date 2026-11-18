package com.example.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.YouTubeApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class PlaybackState(
    val isPlaying: Boolean = false,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val currentTitle: String = "",
    val currentArtist: String = "",
    val currentMediaUrl: String = "",
    val isBuffering: Boolean = false,
    val playbackSpeed: Float = 1.0f
)

class MediaPlaybackService : Service() {

    private val binder = LocalBinder()
    private var mediaPlayer: MediaPlayer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    inner class LocalBinder : Binder() {
        fun getService(): MediaPlaybackService = this@MediaPlaybackService
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        const val ACTION_PLAY = "com.example.service.ACTION_PLAY"
        const val ACTION_PAUSE = "com.example.service.ACTION_PAUSE"
        const val ACTION_STOP = "com.example.service.ACTION_STOP"
        const val ACTION_SEEK_FORWARD = "com.example.service.ACTION_SEEK_FORWARD"
        const val ACTION_SEEK_BACK = "com.example.service.ACTION_SEEK_BACK"
        const val EXTRA_URL = "extra_url"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_ARTIST = "extra_artist"

        private val _playbackState = MutableStateFlow(PlaybackState())
        val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

        fun startService(context: Context, mediaUrl: String, title: String, artist: String) {
            val intent = Intent(context, MediaPlaybackService::class.java).apply {
                putExtra(EXTRA_URL, mediaUrl)
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_ARTIST, artist)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun sendAction(context: Context, action: String) {
            val intent = Intent(context, MediaPlaybackService::class.java).apply {
                this.action = action
            }
            context.startService(intent)
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onCreate() {
        super.onCreate()
        initMediaPlayer()
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnPreparedListener { mp ->
                _playbackState.value = _playbackState.value.copy(
                    isBuffering = false,
                    durationMs = mp.duration,
                    isPlaying = true
                )
                mp.start()
                startProgressTracker()
                updateNotification()
            }
            setOnCompletionListener {
                _playbackState.value = _playbackState.value.copy(
                    isPlaying = false,
                    currentPositionMs = 0
                )
                updateNotification()
            }
            setOnErrorListener { _, _, _ ->
                _playbackState.value = _playbackState.value.copy(isBuffering = false, isPlaying = false)
                true
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_PLAY -> resumePlayback()
                ACTION_PAUSE -> pausePlayback()
                ACTION_STOP -> stopPlayback()
                ACTION_SEEK_FORWARD -> seekTo((mediaPlayer?.currentPosition ?: 0) + 10000)
                ACTION_SEEK_BACK -> seekTo((mediaPlayer?.currentPosition ?: 0) - 10000)
                else -> {
                    val url = it.getStringExtra(EXTRA_URL)
                    val title = it.getStringExtra(EXTRA_TITLE) ?: "YouTube Stream"
                    val artist = it.getStringExtra(EXTRA_ARTIST) ?: "YouTube Creator"
                    if (!url.isNullOrBlank()) {
                        playMedia(url, title, artist)
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    fun playMedia(mediaUrl: String, title: String, artist: String) {
        try {
            _playbackState.value = _playbackState.value.copy(
                currentTitle = title,
                currentArtist = artist,
                currentMediaUrl = mediaUrl,
                isBuffering = true
            )

            startForeground(NOTIFICATION_ID, buildNotification(title, artist, false))

            mediaPlayer?.reset()
            if (mediaUrl.startsWith("content://") || mediaUrl.startsWith("file://")) {
                mediaPlayer?.setDataSource(applicationContext, Uri.parse(mediaUrl))
            } else {
                mediaPlayer?.setDataSource(mediaUrl)
            }
            mediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            e.printStackTrace()
            _playbackState.value = _playbackState.value.copy(isBuffering = false)
        }
    }

    fun resumePlayback() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                _playbackState.value = _playbackState.value.copy(isPlaying = true)
                startProgressTracker()
                updateNotification()
            }
        }
    }

    fun pausePlayback() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _playbackState.value = _playbackState.value.copy(isPlaying = false)
                updateNotification()
            }
        }
    }

    fun stopPlayback() {
        mediaPlayer?.stop()
        _playbackState.value = _playbackState.value.copy(isPlaying = false, currentPositionMs = 0)
        progressJob?.cancel()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    fun seekTo(positionMs: Int) {
        val target = positionMs.coerceIn(0, mediaPlayer?.duration ?: 0)
        mediaPlayer?.seekTo(target)
        _playbackState.value = _playbackState.value.copy(currentPositionMs = target)
    }

    fun setPlaybackSpeed(speed: Float) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer?.playbackParams = mediaPlayer?.playbackParams?.setSpeed(speed) ?: return
            _playbackState.value = _playbackState.value.copy(playbackSpeed = speed)
        }
    }

    private fun startProgressTracker() {
        progressJob?.cancel()
        progressJob = serviceScope.launch {
            while (isActive) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _playbackState.value = _playbackState.value.copy(
                            currentPositionMs = it.currentPosition,
                            durationMs = it.duration
                        )
                    }
                }
                delay(500)
            }
        }
    }

    private fun updateNotification() {
        val state = _playbackState.value
        val notification = buildNotification(state.currentTitle, state.currentArtist, state.isPlaying)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun buildNotification(title: String, artist: String, isPlaying: Boolean): Notification {
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseAction = if (isPlaying) {
            val pauseIntent = PendingIntent.getService(
                this, 1,
                Intent(this, MediaPlaybackService::class.java).apply { action = ACTION_PAUSE },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            NotificationCompat.Action.Builder(
                android.R.drawable.ic_media_pause,
                "Pause",
                pauseIntent
            ).build()
        } else {
            val playIntent = PendingIntent.getService(
                this, 2,
                Intent(this, MediaPlaybackService::class.java).apply { action = ACTION_PLAY },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            NotificationCompat.Action.Builder(
                android.R.drawable.ic_media_play,
                "Play",
                playIntent
            ).build()
        }

        val forwardIntent = PendingIntent.getService(
            this, 3,
            Intent(this, MediaPlaybackService::class.java).apply { action = ACTION_SEEK_FORWARD },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val forwardAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_ff,
            "+10s",
            forwardIntent
        ).build()

        val stopIntent = PendingIntent.getService(
            this, 4,
            Intent(this, MediaPlaybackService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val stopAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Stop",
            stopIntent
        ).build()

        return NotificationCompat.Builder(this, YouTubeApplication.PLAYBACK_CHANNEL_ID)
            .setContentTitle(if (title.isBlank()) "YouTube Playing" else title)
            .setContentText(if (artist.isBlank()) "YouTube Stream" else artist)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(contentIntent)
            .setOngoing(isPlaying)
            .addAction(playPauseAction)
            .addAction(forwardAction)
            .addAction(stopAction)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressJob?.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
        _playbackState.value = PlaybackState()
    }
}
