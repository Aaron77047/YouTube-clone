package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CommentEntity
import com.example.data.model.PlaylistEntity
import com.example.data.model.SubscriptionEntity
import com.example.data.model.VideoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        VideoEntity::class,
        CommentEntity::class,
        PlaylistEntity::class,
        SubscriptionEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun videoDao(): VideoDao
    abstract fun commentDao(): CommentDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "youtube_database"
                )
                    .addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }

            private suspend fun populateDatabase(database: AppDatabase) {
                database.subscriptionDao().insertSubscriptions(SeedData.sampleChannels)
                database.videoDao().insertVideos(SeedData.sampleVideos)
                database.commentDao().insertComments(SeedData.sampleComments)
                database.playlistDao().insertPlaylists(SeedData.samplePlaylists)
            }
        }
    }
}
