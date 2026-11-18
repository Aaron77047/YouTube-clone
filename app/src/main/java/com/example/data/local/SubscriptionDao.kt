package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions ORDER BY channelName ASC")
    fun getAllSubscriptions(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE isSubscribed = 1")
    fun getActiveSubscriptions(): Flow<List<SubscriptionEntity>>

    @Query("SELECT * FROM subscriptions WHERE channelId = :channelId LIMIT 1")
    suspend fun getSubscriptionById(channelId: String): SubscriptionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscription(subscription: SubscriptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscriptions(subscriptions: List<SubscriptionEntity>)

    @Update
    suspend fun updateSubscription(subscription: SubscriptionEntity)

    @Query("UPDATE subscriptions SET isSubscribed = :isSubscribed WHERE channelId = :channelId")
    suspend fun updateSubscriptionStatus(channelId: String, isSubscribed: Boolean)

    @Query("UPDATE subscriptions SET hasNotificationBell = :hasBell WHERE channelId = :channelId")
    suspend fun updateNotificationBell(channelId: String, hasBell: Boolean)
}
