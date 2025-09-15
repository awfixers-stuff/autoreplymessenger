package com.awfixer.autoreplymessenger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.awfixer.autoreplymessenger.data.model.ThreadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThreadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: ThreadEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThreads(threads: List<ThreadEntity>)

    @Update suspend fun updateThread(thread: ThreadEntity)

    @Query("SELECT * FROM threads ORDER BY lastTimestamp DESC")
    fun getAllThreads(): Flow<List<ThreadEntity>>

    @Query("SELECT * FROM threads WHERE id = :threadId")
    fun getThreadById(threadId: Long): Flow<ThreadEntity?>

    @Query("SELECT * FROM threads WHERE participants = :participants LIMIT 1")
    suspend fun getThreadByParticipants(participants: String): ThreadEntity?

    @Query("DELETE FROM threads WHERE id = :threadId") suspend fun deleteThread(threadId: Long)

    @Query("UPDATE threads SET unreadCount = unreadCount + 1 WHERE id = :threadId")
    suspend fun incrementUnreadCount(threadId: Long)

    @Query("UPDATE threads SET unreadCount = 0 WHERE id = :threadId")
    suspend fun markAsRead(threadId: Long)
}
