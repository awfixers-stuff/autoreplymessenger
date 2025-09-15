autoreplymessenger/app/src/main/kotlin/com/awfixer/autoreplymessenger/data/repository/ThreadRepository.kt
package com.awfixer.autoreplymessenger.data.repository

import com.awfixer.autoreplymessenger.data.dao.ThreadDao
import com.awfixer.autoreplymessenger.data.model.ThreadEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Singleton
class ThreadRepository @Inject constructor(private val threadDao: ThreadDao) {

    suspend fun insertThread(thread: ThreadEntity): Long {
        return threadDao.insertThread(thread)
    }

    fun getAllThreads(): Flow<List<ThreadEntity>> {
        return threadDao.getAllThreads()
    }

    fun getThreadById(threadId: Long): Flow<ThreadEntity?> {
        return threadDao.getThreadById(threadId)
    }

    suspend fun getThreadByParticipants(participants: String): ThreadEntity? {
        return threadDao.getThreadByParticipants(participants)
    }

    suspend fun updateThread(thread: ThreadEntity) {
        threadDao.updateThread(thread)
    }

    suspend fun updateThreadLastMessage(threadId: Long, snippet: String, timestamp: Long) {
        val thread = threadDao.getThreadById(threadId).firstOrNull()
        if (thread != null) {
            val updated = thread.copy(lastMessageSnippet = snippet, lastTimestamp = timestamp)
            threadDao.updateThread(updated)
        }
    }

    suspend fun deleteThread(threadId: Long) {
        threadDao.deleteThread(threadId)
    }

    suspend fun incrementUnreadCount(threadId: Long) {
        threadDao.incrementUnreadCount(threadId)
    }

    suspend fun markAsRead(threadId: Long) {
        threadDao.markAsRead(threadId)
    }
}
