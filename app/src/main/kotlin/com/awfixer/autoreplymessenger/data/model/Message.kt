package com.awfixer.autoreplymessenger.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransportType {
    SMS,
    MMS
}

enum class MessageBox {
    INBOX,
    SENT,
    DRAFT
}

@Entity(tableName = "messages")
data class MessageEntity(
        @PrimaryKey(autoGenerate = true) val id: Long = 0,
        val threadId: Long,
        val transportType: TransportType,
        val box: MessageBox,
        val date: Long,
        val address: String,
        val subject: String?,
        val body: String?,
        val status: Int?,
        val errorCode: Int?,
        val mediaPresent: Boolean = false
)
