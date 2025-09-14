package com.awfixer.autoreplymessenger.data.repository

import com.awfixer.autoreplymessenger.data.dao.ConversationDao
import com.awfixer.autoreplymessenger.data.model.Conversation
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class ConversationRepository @Inject constructor(private val conversationDao: ConversationDao) {

    // Get all conversations
    fun getAllConversations(): Flow<List<Conversation>> {
        return conversationDao.getAllConversations()
    }

    // Get conversation by threadId
    fun getConversationByThreadId(threadId: Long): Flow<Conversation?> {
        return conversationDao.getConversationByThreadId(threadId)
    }

    // Insert a new conversation
    suspend fun insertConversation(conversation: Conversation) {
        conversationDao.insertConversation(conversation)
    }

    // Insert multiple conversations
    suspend fun insertConversations(conversations: List<Conversation>) {
        conversationDao.insertConversations(conversations)
    }

    // Update a conversation
    suspend fun updateConversation(conversation: Conversation) {
        conversationDao.updateConversation(conversation)
    }

    // Delete a conversation
    suspend fun deleteConversation(threadId: Long) {
        conversationDao.deleteConversation(threadId)
    }

    // Increment unread count for a conversation
    suspend fun incrementUnreadCount(threadId: Long) {
        conversationDao.incrementUnreadCount(threadId)
    }

    // Mark conversation as read
    suspend fun markAsRead(threadId: Long) {
        conversationDao.markAsRead(threadId)
    }
}
