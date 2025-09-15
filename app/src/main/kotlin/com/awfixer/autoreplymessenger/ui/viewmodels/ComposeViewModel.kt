autoreplymessenger/app/src/main/kotlin/com/awfixer/autoreplymessenger/ui/viewmodels/ComposeViewModel.kt
package com.awfixer.autoreplymessenger.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awfixer.autoreplymessenger.domain.usecase.SendMmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Attachment(val uri: String, val type: String, val size: Long)

@HiltViewModel
class ComposeViewModel @Inject constructor(private val sendMmsUseCase: SendMmsUseCase) : ViewModel() {

    private val _attachments = MutableStateFlow<List<Attachment>>(emptyList())
    val attachments: StateFlow<List<Attachment>> = _attachments

    fun addAttachment(uri: String, type: String, size: Long) {
        val newAttachment = Attachment(uri, type, size)
        _attachments.value = _attachments.value + newAttachment
    }

    fun removeAttachment(attachment: Attachment) {
        _attachments.value = _attachments.value - attachment
    }

    fun sendMms(subject: String, body: String) {
        viewModelScope.launch {
            // Placeholder: Get threadId and recipients from UI
            val threadId = 1L // TODO: Get from navigation or selection
            val recipients = listOf("recipient@example.com") // TODO: Get from UI
            val attachmentUris = _attachments.value.map { it.uri }

            sendMmsUseCase(threadId, recipients, subject, body, attachmentUris)
        }
    }
}
