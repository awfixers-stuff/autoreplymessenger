package com.awfixer.autoreplymessenger.domain.usecase

import com.awfixer.autoreplymessenger.data.model.MessageBox
import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.TransportType
import com.awfixer.autoreplymessenger.data.repository.MessageRepository
import com.awfixer.autoreplymessenger.utils.ApnResolver
import com.awfixer.autoreplymessenger.utils.MmsHttpClient
import javax.inject.Inject

class SendMmsUseCase
@Inject
constructor(
        private val apnResolver: ApnResolver,
        private val mmsHttpClient: MmsHttpClient,
        private val messageRepository: MessageRepository
) {

    suspend operator fun invoke(
            threadId: Long,
            recipients: List<String>,
            subject: String?,
            body: String?,
            attachments: List<String> // URIs
    ): Result<Unit> {
        return try {
            // Resolve APN
            val apn = apnResolver.resolveApn().getOrThrow()

            // Build PDU (placeholder - in real implementation, use PduComposer)
            val pduData = buildPdu(subject, body, attachments)

            // Compress images if needed
            // TODO: Implement compression

            // Store message optimistically as OUTBOX
            val message =
                    MessageEntity(
                            threadId = threadId,
                            transportType = TransportType.MMS,
                            box = MessageBox.SENT,
                            date = System.currentTimeMillis(),
                            address = recipients.joinToString(","),
                            subject = subject,
                            body = body,
                            status = 0, // Sending
                            errorCode = null,
                            mediaPresent = attachments.isNotEmpty()
                    )
            messageRepository.insertMessage(message)

            // Send via HTTP
            val response = mmsHttpClient.sendMms(apn, pduData)

            if (response.isSuccessful) {
                // Update status to SENT
                messageRepository.updateMessage(message.copy(status = 1))
            } else {
                // Update to FAILED
                messageRepository.updateMessage(
                        message.copy(status = -1, errorCode = response.code)
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun buildPdu(subject: String?, body: String?, attachments: List<String>): ByteArray {
        // Placeholder: In real implementation, use Android's MMS libraries or third-party
        return byteArrayOf() // Dummy PDU
    }
}
