package com.example.data.local

import com.example.data.model.CommentEntity
import com.example.data.model.PlaylistEntity
import com.example.data.model.SubscriptionEntity
import com.example.data.model.VideoEntity

object SeedData {

    val sampleChannels = listOf(
        SubscriptionEntity(
            channelId = "c_mkbhd",
            channelName = "Marques Brownlee",
            avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            subscriberCount = "18.5M",
            channelBio = "Quality Tech Videos | YouTuber | Geek | Consumer Electronics Reviewer",
            videoCount = 1620
        ),
        SubscriptionEntity(
            channelId = "c_lofi",
            channelName = "Lofi Girl",
            avatarUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=150&auto=format&fit=crop&q=80",
            subscriberCount = "14.2M",
            channelBio = "peaceful lofi hip hop radio - beats to relax/study to 24/7",
            videoCount = 420
        ),
        SubscriptionEntity(
            channelId = "c_fireship",
            channelName = "Fireship",
            avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            subscriberCount = "3.1M",
            channelBio = "High-intensity code tutorials to help you build apps faster.",
            videoCount = 780
        ),
        SubscriptionEntity(
            channelId = "c_veritasium",
            channelName = "Veritasium",
            avatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
            subscriberCount = "15.8M",
            channelBio = "An element of truth - videos about science, education, and anything interesting.",
            videoCount = 512
        ),
        SubscriptionEntity(
            channelId = "c_ign",
            channelName = "IGN Gaming",
            avatarUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=150&auto=format&fit=crop&q=80",
            subscriberCount = "17.9M",
            channelBio = "The latest game reviews, trailers, walkthroughs, and news.",
            videoCount = 12400
        )
    )

    val sampleVideos = listOf(
        VideoEntity(
            id = 1,
            title = "Building the Ultimate Minimalist Android Setup in 2026",
            description = "A complete deep-dive tour of our new OLED clean workstation, custom mechanical keyboards, and modern app developments. We explore ultra-fluid refresh rates, battery optimizations, and the philosophy behind distraction-free digital productivity.",
            creatorName = "Marques Brownlee",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&auto=format&fit=crop&q=80",
            channelId = "c_mkbhd",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop&q=80",
            durationSeconds = 634,
            viewsCount = 1420500,
            likesCount = 89400,
            uploadTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 3, // 3 hours ago
            isMusic = false,
            category = "Tech",
            tags = "Tech, Setup, Desk, OLED, Android"
        ),
        VideoEntity(
            id = 2,
            title = "Midnight Chill Beats | Lofi Hip Hop Stream for Deep Focus & Coding",
            description = "Relaxing, atmospheric lofi music curated for late-night programming sessions, studying for university finals, or winding down. Smooth Rhodes electric pianos, mellow acoustic drums, and gentle tape hiss warmth.",
            creatorName = "Lofi Girl",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=150&auto=format&fit=crop&q=80",
            channelId = "c_lofi",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80",
            durationSeconds = 1840,
            viewsCount = 3890200,
            likesCount = 245000,
            uploadTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 12,
            isMusic = true,
            category = "Music",
            tags = "Lofi, Music, Chill, Coding, Study"
        ),
        VideoEntity(
            id = 3,
            title = "The Quantum Computing Breakthrough Nobody Expected",
            description = "How error-corrected logical qubits changed everything this year. We visited the top laboratory in California to test quantum superposition, quantum entanglement algorithms, and what it implies for the future of cryptography.",
            creatorName = "Veritasium",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
            channelId = "c_veritasium",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800&auto=format&fit=crop&q=80",
            durationSeconds = 852,
            viewsCount = 2410800,
            likesCount = 178000,
            uploadTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24,
            isMusic = false,
            category = "Science",
            tags = "Science, Quantum, Physics, Tech"
        ),
        VideoEntity(
            id = 4,
            title = "Next-Gen Unreal Engine 5.6 Action RPG Gameplay Reveal",
            description = "Exclusive world premiere trailer and 15-minute uninterrupted gameplay walkthrough running on next-generation hardware at native 4K 120FPS with Nanite ray-traced reflections.",
            creatorName = "IGN Gaming",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1566492031773-4f4e44671857?w=150&auto=format&fit=crop&q=80",
            channelId = "c_ign",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80",
            durationSeconds = 745,
            viewsCount = 940000,
            likesCount = 61200,
            uploadTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 48,
            isMusic = false,
            category = "Gaming",
            tags = "Gaming, RPG, UE5, NextGen, Trailer"
        ),
        VideoEntity(
            id = 5,
            title = "Modern Kotlin Coroutines & Flow in 100 Seconds",
            description = "Learn how asynchronous Kotlin programming works under the hood: structured concurrency, dispatchers, cancellation, StateFlow vs SharedFlow, and how to avoid blocking the Android UI thread.",
            creatorName = "Fireship",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            channelId = "c_fireship",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800&auto=format&fit=crop&q=80",
            durationSeconds = 142,
            viewsCount = 890400,
            likesCount = 98500,
            uploadTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 72,
            isMusic = false,
            category = "Coding",
            tags = "Coding, Kotlin, JetpackCompose, Tutorial"
        ),
        VideoEntity(
            id = 6,
            title = "Synthwave Neon Odyssey - Retro Electro Mix 2026",
            description = "High octane analog synthesizers, cyberpunk basslines, driving 80s drums, and nostalgic highway driving anthems. Put on your headphones and dive into the neon grid.",
            creatorName = "Lofi Girl",
            creatorAvatarUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=150&auto=format&fit=crop&q=80",
            channelId = "c_lofi",
            mediaUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackSeeTheWorld.mp4",
            thumbnailUrl = "https://images.unsplash.com/photo-1508700115892-45ecd05ae2ad?w=800&auto=format&fit=crop&q=80",
            durationSeconds = 1240,
            viewsCount = 1120400,
            likesCount = 94300,
            uploadTimestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 96,
            isMusic = true,
            category = "Music",
            tags = "Synthwave, Electronic, Cyberpunk, Music"
        )
    )

    val sampleComments = listOf(
        CommentEntity(
            id = 1,
            videoId = 1,
            authorName = "Alex Rivera",
            authorAvatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150&auto=format&fit=crop&q=80",
            content = "The OLED contrast on this setup looks unbelievable. Best video production quality on the platform as always!",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 45,
            likesCount = 342,
            isLikedByMe = true
        ),
        CommentEntity(
            id = 2,
            videoId = 1,
            authorName = "TechEnthusiast99",
            authorAvatarUrl = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150&auto=format&fit=crop&q=80",
            content = "Can you share the wallpaper link from the desk monitor? Great video Marques!",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 120,
            likesCount = 89,
            isLikedByMe = false
        ),
        CommentEntity(
            id = 3,
            videoId = 2,
            authorName = "Samantha Wu",
            authorAvatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&auto=format&fit=crop&q=80",
            content = "Helped me survive studying through a 6-hour software engineering sprint. Bless this channel ❤️",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 180,
            likesCount = 512,
            isLikedByMe = true
        ),
        CommentEntity(
            id = 4,
            videoId = 5,
            authorName = "DevJordan",
            authorAvatarUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&auto=format&fit=crop&q=80",
            content = "This explanation made coroutines finally click for me after months of confusion. Fireship does it again!",
            timestamp = System.currentTimeMillis() - 1000 * 60 * 300,
            likesCount = 184,
            isLikedByMe = false
        )
    )

    val samplePlaylists = listOf(
        PlaylistEntity(
            id = 1,
            title = "Watch Later",
            description = "Saved videos to catch up on",
            videoIds = "1,3,5",
            isPrivate = true,
            coverThumbnailUrl = "https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop&q=80"
        ),
        PlaylistEntity(
            id = 2,
            title = "Favorite Music & Lofi",
            description = "Vibes for late night programming and chill study",
            videoIds = "2,6",
            isPrivate = false,
            coverThumbnailUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800&auto=format&fit=crop&q=80"
        ),
        PlaylistEntity(
            id = 3,
            title = "Tech & Science Deep Dives",
            description = "Hardware reviews and mind-bending physics discoveries",
            videoIds = "1,3,4",
            isPrivate = false,
            coverThumbnailUrl = "https://images.unsplash.com/photo-1635070041078-e363dbe005cb?w=800&auto=format&fit=crop&q=80"
        )
    )
}
