autoreplymessenger/app/src/main/kotlin/com/awfixer/autoreplymessenger/ui/composables/InboxScreen.kt
package com.awfixer.autoreplymessenger.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.awfixer.autoreplymessenger.R
import com.awfixer.autoreplymessenger.data.model.ThreadEntity
import com.awfixer.autoreplymessenger.ui.viewmodels.InboxViewModel

@Composable
fun InboxScreen(navController: NavController) {
    val viewModel: InboxViewModel = hiltViewModel()
    val threads by viewModel.threads.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.inbox)) },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("compose") }) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.compose))
            }
        }
    ) { padding ->
        if (threads.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text(stringResource(R.string.no_conversations)) }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(threads) { thread ->
                    ThreadItem(
                        thread = thread,
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
fun ThreadItem(thread: ThreadEntity, onClick: () -> Unit) {
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
                text = thread.participants.firstOrNull()?.toString() ?: stringResource(R.string.unknown),
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = thread.participants,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = thread.lastMessageSnippet ?: "",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (thread.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Badge { Text(thread.unreadCount.toString()) }
        }
    }
}
