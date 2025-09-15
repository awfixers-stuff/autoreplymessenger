package com.awfixer.autoreplymessenger.data.repository

import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.MmsPartEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MmsRepository @Inject constructor(private val messageDao: MessageDao) {

    suspend fun insertMmsMessage(message: MessageEntity): Long {
        return messageDao.insertMessage(message)
    }

    suspend fun insertMmsParts(parts: List<MmsPartEntity>) {
        messageDao.insertMmsParts(parts)
    }

    suspend fun getMmsPartsForMessage(messageId: Long): List<MmsPartEntity> {
        return messageDao.getMmsPartsForMessage(messageId)
    }

    suspend fun updateMmsMessage(message: MessageEntity) {
        messageDao.updateMessage(message)
    }

    suspend fun deleteMmsMessage(messageId: Long) {
        messageDao.deleteMessage(messageId)
    }
}
