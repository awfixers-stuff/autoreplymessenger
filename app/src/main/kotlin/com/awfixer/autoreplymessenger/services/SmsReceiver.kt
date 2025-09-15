package com.awfixer.autoreplymessenger.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import com.awfixer.autoreplymessenger.data.model.Message
import com.awfixer.autoreplymessenger.data.repository.AutoReplyRepository
import com.awfixer.autoreplymessenger.data.repository.MessageRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var messageRepository: MessageRepository
    @Inject lateinit var autoReplyRepository: AutoReplyRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (smsMessage in messages) {
                val sender = smsMessage.originatingAddress ?: ""
                val body = smsMessage.messageBody
                // Use sender hashCode as threadId for simplicity
                val threadId = sender.hashCode().toLong()

                val message =
                        Message(
                                threadId = threadId,
                                sender = sender,
                                body = body,
                                timestamp = smsMessage.timestampMillis,
                                type = 1, // SMS
                                isIncoming = true,
                                isRead = false
                        )
                CoroutineScope(Dispatchers.IO).launch {
                    messageRepository.insertMessage(message)
                    autoReplyRepository.handleIncomingSms(sender, body)
                }
            }
        }
    }
}
