package com.awfixer.autoreplymessenger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.MmsPartEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Update suspend fun updateMessage(message: MessageEntity)

    @Query("SELECT * FROM messages WHERE id = :messageId")
    suspend fun getMessageById(messageId: Long): MessageEntity?

    @Query("SELECT * FROM messages WHERE threadId = :threadId ORDER BY date DESC")
    fun getMessagesForThread(threadId: Long): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages ORDER BY date DESC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Query("SELECT * FROM messages WHERE box = 'INBOX' AND status IS NULL")
    fun getUnreadMessages(): Flow<List<MessageEntity>>

    @Query("DELETE FROM messages WHERE id = :messageId") suspend fun deleteMessage(messageId: Long)

    @Query("DELETE FROM messages WHERE threadId = :threadId")
    suspend fun deleteMessagesForThread(threadId: Long)

    // MMS Parts
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMmsPart(part: MmsPartEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMmsParts(parts: List<MmsPartEntity>)

    @Query("SELECT * FROM mms_parts WHERE messageId = :messageId ORDER BY seq")
    suspend fun getMmsPartsForMessage(messageId: Long): List<MmsPartEntity>

    @Query("DELETE FROM mms_parts WHERE messageId = :messageId")
    suspend fun deleteMmsPartsForMessage(messageId: Long)
}
