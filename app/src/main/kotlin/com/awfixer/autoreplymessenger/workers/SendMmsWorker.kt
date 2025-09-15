package com.awfixer.autoreplymessenger.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.awfixer.autoreplymessenger.domain.usecase.SendMmsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendMmsWorker
@AssistedInject
constructor(
        @Assisted private val context: Context,
        @Assisted private val workerParams: WorkerParameters,
        private val sendMmsUseCase: SendMmsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val threadId = inputData.getLong("thread_id", -1)
            val recipients = inputData.getStringArray("recipients") ?: emptyArray()
            val subject = inputData.getString("subject")
            val body = inputData.getString("body")
            val attachments = inputData.getStringArray("attachments") ?: emptyArray()

            if (threadId == -1L || recipients.isEmpty()) {
                return Result.failure()
            }

            val result =
                    sendMmsUseCase(
                            threadId,
                            recipients.toList(),
                            subject,
                            body,
                            attachments.toList()
                    )

            if (result.isSuccess) {
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}
