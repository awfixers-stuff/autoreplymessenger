package com.awfixer.autoreplymessenger.domain.usecase

import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.MmsPartEntity
import com.awfixer.autoreplymessenger.data.model.ThreadEntity
import com.awfixer.autoreplymessenger.data.repository.MessageRepository
import com.awfixer.autoreplymessenger.data.repository.ThreadRepository
import javax.inject.Inject

class IngestMmsIntoThreadUseCase
@Inject
constructor(
        private val messageRepository: MessageRepository,
        private val threadRepository: ThreadRepository
) {

    suspend operator fun invoke(message: MessageEntity, parts: List<MmsPartEntity>) {
        // Find or create thread based on address
        val threadId = findOrCreateThread(message.address)

        // Update message with threadId
        val updatedMessage = message.copy(threadId = threadId)

        // Insert message
        val messageId = messageRepository.insertMessage(updatedMessage)

        // Update parts with messageId
        val updatedParts = parts.map { it.copy(messageId = messageId) }

        // Insert parts
        messageRepository.insertMmsParts(updatedParts)

        // Update thread's last message snippet and timestamp
        val snippet = message.subject ?: message.body ?: "MMS"
        threadRepository.updateThreadLastMessage(threadId, snippet, message.date)
    }

    private suspend fun findOrCreateThread(address: String): Long {
        // For simplicity, use address as participant; in real app, handle multiple
        val existingThread = threadRepository.getThreadByParticipants(address)
        return if (existingThread != null) {
            existingThread.id
        } else {
            val newThread =
                    ThreadEntity(
                            lastMessageSnippet = null,
                            lastTimestamp = 0,
                            participants = address,
                            unreadCount = 1
                    )
            threadRepository.insertThread(newThread)
        }
    }
}
