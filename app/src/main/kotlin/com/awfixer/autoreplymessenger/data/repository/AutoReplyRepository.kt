package com.awfixer.autoreplymessenger.data.repository

import android.content.Context
import android.telephony.SmsManager
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.awfixer.autoreplymessenger.data.model.Message
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutoReplyRepository
@Inject
constructor(
        @ApplicationContext private val context: Context,
        private val messageRepository: MessageRepository
) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val smsManager = SmsManager.getDefault()

    fun isAutoReplyEnabled(): Boolean {
        return prefs.getBoolean("auto_reply_enabled", false)
    }

    fun getReplyTemplate(): String {
        return prefs.getString("reply_template", "I'm using Signal now—please contact me there.")
                ?: ""
    }

    suspend fun handleIncomingSms(sender: String, body: String) {
        if (!isAutoReplyEnabled()) return

        // Check conditions (e.g., time, contacts) - for now, always reply
        val replyMessage = getReplyTemplate()

        // Send SMS reply
        sendSms(sender, replyMessage)

        // Save the reply as outgoing message
        val threadId = sender.hashCode().toLong()
        val message =
                Message(
                        threadId = threadId,
                        sender = "Me",
                        body = replyMessage,
                        timestamp = System.currentTimeMillis(),
                        type = 1,
                        isIncoming = false,
                        isRead = true
                )
        messageRepository.insertMessage(message)
    }

    suspend fun handleIncomingCall(incomingNumber: String) {
        if (!isAutoReplyEnabled()) return

        // For calls, decline and send SMS
        // Note: Declining calls requires TelecomManager, but for simplicity, just send SMS
        val replyMessage = getReplyTemplate()

        sendSms(incomingNumber, replyMessage)

        // Save the reply
        val threadId = incomingNumber.hashCode().toLong()
        val message =
                Message(
                        threadId = threadId,
                        sender = "Me",
                        body = replyMessage,
                        timestamp = System.currentTimeMillis(),
                        type = 1,
                        isIncoming = false,
                        isRead = true
                )
        messageRepository.insertMessage(message)
    }

    private fun sendSms(phoneNumber: String, message: String) {
        try {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
        } catch (e: Exception) {
            // Handle error, e.g., log or show toast
        }
    }

    fun setAutoReplyEnabled(enabled: Boolean) {
        prefs.edit { putBoolean("auto_reply_enabled", enabled) }
    }

    fun setReplyTemplate(template: String) {
        prefs.edit { putString("reply_template", template) }
    }
}
