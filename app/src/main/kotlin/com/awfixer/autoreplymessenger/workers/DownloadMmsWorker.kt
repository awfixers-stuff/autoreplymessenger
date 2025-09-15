package com.awfixer.autoreplymessenger.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.awfixer.autoreplymessenger.domain.usecase.ReceiveMmsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DownloadMmsWorker
@AssistedInject
constructor(
        @Assisted private val context: Context,
        @Assisted private val workerParams: WorkerParameters,
        private val receiveMmsUseCase: ReceiveMmsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val pduData = inputData.getByteArray("pdu_data") ?: return Result.failure()

            // Simplified: Assume pduData is RetrieveConf and process directly
            // In full implementation, download from MMSC if it's NotificationInd
            receiveMmsUseCase(pduData).getOrElse {
                return Result.failure()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
