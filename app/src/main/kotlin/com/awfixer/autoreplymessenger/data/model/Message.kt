package com.awfixer.autoreplymessenger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val threadId: Long,
        val sender: String,
        val body: String,
        val timestamp: Long,
        val type: Int, // 1 for SMS, 2 for MMS
        val isIncoming: Boolean,
        val isRead: Boolean = false,
        val attachments: List<String>? = null // For MMS, list of URIs or paths
)
