package com.awfixer.autoreplymessenger.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awfixer.autoreplymessenger.data.model.Conversation
import com.awfixer.autoreplymessenger.data.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class InboxViewModel
@Inject
constructor(private val conversationRepository: ConversationRepository) : ViewModel() {

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
