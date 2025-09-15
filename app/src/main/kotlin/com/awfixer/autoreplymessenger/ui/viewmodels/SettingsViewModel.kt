package com.awfixer.autoreplymessenger.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awfixer.autoreplymessenger.data.repository.AutoReplyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(private val autoReplyRepository: AutoReplyRepository) :
        ViewModel() {

    private val _autoReplyEnabled = MutableStateFlow(false)
    val autoReplyEnabled: StateFlow<Boolean> = _autoReplyEnabled

    private val _replyTemplate = MutableStateFlow("")
    val replyTemplate: StateFlow<String> = _replyTemplate

    init {
        loadSettings()
    }

    private fun loadSettings() {
        _autoReplyEnabled.value = autoReplyRepository.isAutoReplyEnabled()
        _replyTemplate.value = autoReplyRepository.getReplyTemplate()
    }

    fun setAutoReplyEnabled(enabled: Boolean) {
        viewModelScope.launch {
            autoReplyRepository.setAutoReplyEnabled(enabled)
            _autoReplyEnabled.value = enabled
        }
    }

    fun setReplyTemplate(template: String) {
        viewModelScope.launch {
            autoReplyRepository.setReplyTemplate(template)
            _replyTemplate.value = template
        }
    }
}
