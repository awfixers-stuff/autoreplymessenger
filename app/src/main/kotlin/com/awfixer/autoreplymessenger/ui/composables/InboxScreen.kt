package com.awfixer.autoreplymessenger.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awfixer.autoreplymessenger.data.model.Conversation
import com.awfixer.autoreplymessenger.ui.viewmodels.InboxViewModel

@Composable
fun InboxScreen(navController: NavController) {
    val viewModel: InboxViewModel = hiltViewModel()
    val conversations by viewModel.conversations.collectAsState()

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Inbox") },
                        actions = {
                            IconButton(onClick = { /* TODO: Search */}) {
                                Icon(Search, contentDescription = "Search")
                            }
                        }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("compose") }) {
                    Icon(Add, contentDescription = "Compose")
                }
            }
    ) { padding ->
        if (conversations.isEmpty()) {
            Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
            ) { Text("No conversations yet") }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(conversations) { conversation ->
                    ConversationItem(
                            conversation = conversation,
                            onClick = {
                                // TODO: Navigate to conversation details
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun ConversationItem(conversation: Conversation, onClick: () -> Unit) {
    Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar placeholder
        Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                    text = conversation.contactName?.firstOrNull()?.toString()
                                    ?: conversation.contactNumber.firstOrNull()?.toString() ?: "?",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                    text = conversation.contactName ?: conversation.contactNumber,
                    style = MaterialTheme.typography.titleMedium
            )
            Text(
                    text = conversation.snippet,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )
        }

        if (conversation.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Badge { Text(conversation.unreadCount.toString()) }
        }
    }
}
