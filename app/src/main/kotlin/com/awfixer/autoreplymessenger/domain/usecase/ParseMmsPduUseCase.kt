package com.awfixer.autoreplymessenger.domain.usecase

import com.awfixer.autoreplymessenger.data.model.ParsedMms
import com.awfixer.autoreplymessenger.utils.MmsPduParser
import javax.inject.Inject

class ParseMmsPduUseCase @Inject constructor(private val mmsPduParser: MmsPduParser) {

    operator fun invoke(pduData: ByteArray): Result<ParsedMms> {
        return try {
            // Assuming it's RetrieveConf for now; in practice, check PDU type
            val parsed = mmsPduParser.parseRetrieveConf(pduData)
            if (parsed != null) {
                Result.success(parsed)
            } else {
                Result.failure(Exception("Failed to parse MMS PDU"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
