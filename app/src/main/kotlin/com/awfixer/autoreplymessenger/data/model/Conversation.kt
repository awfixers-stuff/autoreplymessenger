package com.awfixer.autoreplymessenger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "threads")
data class ThreadEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val lastMessageSnippet: String?,
        val lastTimestamp: Long,
        val participants: String, // JSON or comma-separated
        val unreadCount: Int = 0
)
