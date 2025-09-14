package com.awfixer.autoreplymessenger.data.repository

import com.awfixer.autoreplymessenger.data.dao.ConversationDao
import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.model.Conversation
import com.awfixer.autoreplymessenger.data.model.Message
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Singleton
class MessageRepository
@Inject
constructor(private val messageDao: MessageDao, private val conversationDao: ConversationDao) {

    // Insert a new message and update the corresponding conversation
    suspend fun insertMessage(message: Message) {
        val messageId = messageDao.insertMessage(message)
        updateConversationForMessage(message)
    }

    // Insert multiple messages
    suspend fun insertMessages(messages: List<Message>) {
        messageDao.insertMessages(messages)
        messages.forEach { updateConversationForMessage(it) }
    }

    // Get messages for a specific thread
    fun getMessagesForThread(threadId: Long): Flow<List<Message>> {
        return messageDao.getMessagesForThread(threadId)
    }

    // Get all messages
    fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages()
    }

    // Get unread messages
    fun getUnreadMessages(): Flow<List<Message>> {
        return messageDao.getUnreadMessages()
    }

    // Update a message
    suspend fun updateMessage(message: Message) {
        messageDao.updateMessage(message)
        updateConversationForMessage(message)
    }

    // Delete a message
    suspend fun deleteMessage(messageId: Long) {
        // Get the message to know the threadId
        val message = messageDao.getMessageById(messageId)
        if (message != null) {
            messageDao.deleteMessage(messageId)
            updateConversationForMessage(message)
        }
    }

    // Delete all messages for a thread
    suspend fun deleteMessagesForThread(threadId: Long) {
        messageDao.deleteMessagesForThread(threadId)
        conversationDao.deleteConversation(threadId)
    }

    // Helper function to update conversation based on message
    private suspend fun updateConversationForMessage(message: Message) {
        val conversation = conversationDao.getConversationByThreadId(message.threadId).first()
        if (conversation == null) {
            // Create new conversation
            val newConversation =
                    Conversation(
                            threadId = message.threadId,
                            contactName = null, // TODO: Fetch from contacts
                            contactNumber = message.sender,
                            lastMessage = message.body,
                            lastMessageTimestamp = message.timestamp,
                            unreadCount = if (message.isIncoming && !message.isRead) 1 else 0,
                            snippet = message.body.take(50)
                    )
            conversationDao.insertConversation(newConversation)
        } else {
            // Update existing conversation
            val updatedConversation =
                    conversation.copy(
                            lastMessage = message.body,
                            lastMessageTimestamp = message.timestamp,
                            unreadCount =
                                    if (message.isIncoming && !message.isRead)
                                            conversation.unreadCount + 1
                                    else conversation.unreadCount,
                            snippet = message.body.take(50)
                    )
            conversationDao.updateConversation(updatedConversation)
        }
    }
}
