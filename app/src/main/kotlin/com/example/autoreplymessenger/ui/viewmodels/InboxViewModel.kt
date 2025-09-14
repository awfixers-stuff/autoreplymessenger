<file_path>
autoreplyapp/app/src/main/kotlin/com/example/autoreplymessenger/ui/viewmodels/InboxViewModel.kt
</file_path>

<edit_description>
Create InboxViewModel.kt
</edit_description>

package com.example.autoreplymessenger.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autoreplymessenger.data.model.Conversation
import com.example.autoreplymessenger.data.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class InboxViewModel @Inject constructor(private val conversationRepository: ConversationRepository) : ViewModel() {

    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    val conversations: StateFlow<List<Conversation>> = _conversations

    init {
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            conversationRepository.getAllConversations().collectLatest { conversations ->
                _conversations.value = conversations
            }
        }
    }
}
