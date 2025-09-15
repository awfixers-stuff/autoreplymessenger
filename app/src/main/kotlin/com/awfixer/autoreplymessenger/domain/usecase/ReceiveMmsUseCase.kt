package com.awfixer.autoreplymessenger.domain.usecase

import com.awfixer.autoreplymessenger.data.model.MessageBox
import com.awfixer.autoreplymessenger.data.model.MessageEntity
import com.awfixer.autoreplymessenger.data.model.MmsPartEntity
import com.awfixer.autoreplymessenger.data.model.TransportType
import com.awfixer.autoreplymessenger.data.repository.MmsRepository
import javax.inject.Inject

class ReceiveMmsUseCase
@Inject
constructor(
        private val mmsRepository: MmsRepository,
        private val parseMmsPduUseCase: ParseMmsPduUseCase,
        private val ingestMmsIntoThreadUseCase: IngestMmsIntoThreadUseCase
) {

    suspend operator fun invoke(pduData: ByteArray): Result<Unit> {
        return try {
            // Parse the PDU
            val parsedMms = parseMmsPduUseCase(pduData).getOrThrow()

            // Create MessageEntity
            val message =
                    MessageEntity(
                            threadId = 0, // Will be set by ingest
                            transportType = TransportType.MMS,
                            box = MessageBox.INBOX,
                            date = parsedMms.date,
                            address = parsedMms.from,
                            subject = parsedMms.subject,
                            body = parsedMms.body,
                            status = null,
                            errorCode = null,
                            mediaPresent = parsedMms.parts.isNotEmpty()
                    )

            // Create parts
            val parts =
                    parsedMms.parts.map { part ->
                        MmsPartEntity(
                                messageId = 0, // Will be set after insert
                                seq = part.seq,
                                contentType = part.contentType,
                                filename = part.filename,
                                text = part.text,
                                fileUri = part.fileUri
                        )
                    }

            // Ingest into thread
            ingestMmsIntoThreadUseCase(message, parts)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
