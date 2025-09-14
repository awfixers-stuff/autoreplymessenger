package com.example.autoreplymessenger.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.autoreplymessenger.data.dao.ConversationDao
import com.example.autoreplymessenger.data.dao.MessageDao
import com.example.autoreplymessenger.data.model.Conversation
import com.example.autoreplymessenger.data.model.Message

@Database(entities = [Message::class, Conversation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
}
