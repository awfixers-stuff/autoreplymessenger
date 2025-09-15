autoreplymessenger/app/src/main/kotlin/com/awfixer/autoreplymessenger/data/model/MmsPartEntity.kt
package com.awfixer.autoreplymessenger.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "mms_parts",
    foreignKeys = [
        ForeignKey(
            entity = MessageEntity::class,
            parentColumns = ["id"],
            childColumns = ["messageId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MmsPartEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val messageId: Long,
    val seq: Int,
    val contentType: String,
    val filename: String?,
    val text: String?,
    val fileUri: String?
)
