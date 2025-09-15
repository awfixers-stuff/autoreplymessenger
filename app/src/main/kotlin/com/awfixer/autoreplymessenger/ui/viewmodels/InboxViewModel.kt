package com.awfixer.autoreplymessenger.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awfixer.autoreplymessenger.data.model.ThreadEntity
import com.awfixer.autoreplymessenger.data.repository.ThreadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class InboxViewModel @Inject constructor(private val threadRepository: ThreadRepository) :
        ViewModel() {

    private val _threads = MutableStateFlow<List<ThreadEntity>>(emptyList())
    val threads: StateFlow<List<ThreadEntity>> = _threads

    init {
        loadThreads()
    }

    private fun loadThreads() {
        viewModelScope.launch {
            threadRepository.getAllThreads().collectLatest { threads -> _threads.value = threads }
        }
    }
}
