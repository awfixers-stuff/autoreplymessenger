package com.awfixer.autoreplymessenger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drafts")
data class DraftEntity(
        @PrimaryKey val threadId: Long,
        val text: String?,
        val attachments: String? // JSON list of URIs
)
