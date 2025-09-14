<file_path>
autoreplyapp/app/src/main/kotlin/com/example/autoreplymessenger/services/SmsReceiver.kt
</file_path>

<edit_description>
Create SmsReceiver.kt
</edit_description>

package com.example.autoreplymessenger.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.example.autoreplymessenger.data.model.Message
import com.example.autoreplymessenger.data.repository.MessageRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var messageRepository: MessageRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (smsMessage in messages) {
                val message = Message(
                        threadId = smsMessage.threadId,
                        sender = smsMessage.originatingAddress ?: "",
                        body = smsMessage.messageBody,
                        timestamp = smsMessage.timestampMillis,
                        type = 1, // SMS
                        isIncoming = true,
                        isRead = false
                )
                CoroutineScope(Dispatchers.IO).launch {
                    messageRepository.insertMessage(message)
                }
            }
        }
    }
}
