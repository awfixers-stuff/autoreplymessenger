package com.awfixer.autoreplymessenger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversations")
data class Conversation(
        @PrimaryKey val threadId: Long,
        val contactName: String?,
        val contactNumber: String,
        val lastMessage: String,
        val lastMessageTimestamp: Long,
        val unreadCount: Int = 0,
        val snippet: String
)
