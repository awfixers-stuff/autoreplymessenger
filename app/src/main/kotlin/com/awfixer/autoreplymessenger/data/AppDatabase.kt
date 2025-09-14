package com.awfixer.autoreplymessenger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.awfixer.autoreplymessenger.data.dao.ConversationDao
import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.model.Conversation
import com.awfixer.autoreplymessenger.data.model.Message

@Database(entities = [Message::class, Conversation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
}
