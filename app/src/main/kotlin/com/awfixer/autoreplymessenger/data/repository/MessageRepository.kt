package com.awfixer.autoreplymessenger.data.repository

import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.dao.ThreadDao
import com.awfixer.autoreplymessenger.data.model.MessageBox
import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.MmsPartEntity
import com.awfixer.autoreplymessenger.data.model.ThreadEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Singleton
class MessageRepository
@Inject
constructor(private val messageDao: MessageDao, private val threadDao: ThreadDao) {

    // Insert a new message and update the corresponding thread
    suspend fun insertMessage(message: MessageEntity): Long {
        val messageId = messageDao.insertMessage(message)
        updateThreadForMessage(message)
        return messageId
    }

    // Insert multiple messages
    suspend fun insertMessages(messages: List<MessageEntity>) {
        messageDao.insertMessages(messages)
        messages.forEach { updateThreadForMessage(it) }
    }

    // Get messages for a specific thread
    fun getMessagesForThread(threadId: Long): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForThread(threadId)
    }

    // Get all messages
    fun getAllMessages(): Flow<List<MessageEntity>> {
        return messageDao.getAllMessages()
    }

    // Get unread messages
    fun getUnreadMessages(): Flow<List<MessageEntity>> {
        return messageDao.getUnreadMessages()
    }

    // Update a message
    suspend fun updateMessage(message: MessageEntity) {
        messageDao.updateMessage(message)
        updateThreadForMessage(message)
    }

    // Delete a message
    suspend fun deleteMessage(messageId: Long) {
        // Get the message to know the threadId
        val message = messageDao.getMessageById(messageId)
        if (message != null) {
            messageDao.deleteMessage(messageId)
            messageDao.deleteMmsPartsForMessage(messageId)
            updateThreadForMessage(message)
        }
    }

    // Delete all messages for a thread
    suspend fun deleteMessagesForThread(threadId: Long) {
        messageDao.deleteMessagesForThread(threadId)
        threadDao.deleteThread(threadId)
    }

    // MMS Parts
    suspend fun insertMmsPart(part: MmsPartEntity): Long {
        return messageDao.insertMmsPart(part)
    }

    suspend fun insertMmsParts(parts: List<MmsPartEntity>) {
        messageDao.insertMmsParts(parts)
    }

    suspend fun getMmsPartsForMessage(messageId: Long): List<MmsPartEntity> {
        return messageDao.getMmsPartsForMessage(messageId)
    }

    // Helper function to update thread based on message
    private suspend fun updateThreadForMessage(message: MessageEntity) {
        val thread = threadDao.getThreadById(message.threadId).first()
        if (thread == null) {
            // Create new thread
            val newThread =
                    ThreadEntity(
                            lastMessageSnippet = message.subject ?: message.body ?: "New message",
                            lastTimestamp = message.date,
                            participants = message.address,
                            unreadCount = if (message.box == MessageBox.INBOX) 1 else 0
                    )
            threadDao.insertThread(newThread)
        } else {
            // Update existing thread
            val updatedThread =
                    thread.copy(
                            lastMessageSnippet = message.subject
                                            ?: message.body ?: thread.lastMessageSnippet,
                            lastTimestamp = message.date,
                            unreadCount =
                                    if (message.box == MessageBox.INBOX) thread.unreadCount + 1
                                    else thread.unreadCount
                    )
            threadDao.updateThread(updatedThread)
        }
    }
}
