package com.awfixer.autoreplymessenger.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.awfixer.autoreplymessenger.domain.usecase.ReceiveMmsUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MmsReceiver : BroadcastReceiver() {

    @Inject lateinit var receiveMmsUseCase: ReceiveMmsUseCase
    @Inject lateinit var workManager: WorkManager

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.WAP_PUSH_RECEIVED_ACTION) {
            val pduData = intent.getByteArrayExtra("data") ?: return

            // Simplified: Assume it's a notification and schedule download
            // In full implementation, parse PDU to check type
            val data = Data.Builder().putByteArray("pdu_data", pduData).build()
            val downloadWorkRequest =
                    OneTimeWorkRequestBuilder<DownloadMmsWorker>().setInputData(data).build()
            workManager.enqueue(downloadWorkRequest)
        }
    }
}
