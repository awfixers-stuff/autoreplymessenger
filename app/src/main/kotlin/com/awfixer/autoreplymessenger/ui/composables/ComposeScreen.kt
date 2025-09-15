package com.awfixer.autoreplymessenger.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.awfixer.autoreplymessenger.R
import com.awfixer.autoreplymessenger.ui.viewmodels.ComposeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeScreen(navController: androidx.navigation.NavController) {
    val viewModel: ComposeViewModel = hiltViewModel()
    val context = LocalContext.current

    var subject by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    val attachments by viewModel.attachments.collectAsState()

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text(stringResource(R.string.compose)) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                        Icons.Default.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                )
                            }
                        },
                        actions = {
                            TextButton(
                                    onClick = {
                                        // TODO: Send MMS
                                        viewModel.sendMms(subject, body)
                                        navController.popBackStack()
                                    }
                            ) { Text(stringResource(R.string.send)) }
                        }
                )
            }
    ) { padding ->
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(padding)
                                .verticalScroll(rememberScrollState())
        ) {
            // Recipients (placeholder)
            OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text(stringResource(R.string.to)) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    placeholder = { Text("Enter recipient") }
            )

            // Subject
            OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text(stringResource(R.string.subject)) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    singleLine = true
            )

            // Attachment bar
            Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(onClick = { /* Open camera */}) {
                    Icon(Icons.Default.Camera, contentDescription = stringResource(R.string.camera))
                }
                IconButton(onClick = { /* Open gallery */}) {
                    Icon(Icons.Default.Image, contentDescription = stringResource(R.string.gallery))
                }
                IconButton(onClick = { /* Open file picker */}) {
                    Icon(
                            Icons.Default.AttachFile,
                            contentDescription = stringResource(R.string.attach_file)
                    )
                }
            }

            // Attachments preview
            if (attachments.isNotEmpty()) {
                LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(attachments) { attachment ->
                        AttachmentThumbnail(
                                uri = attachment.uri,
                                onRemove = { viewModel.removeAttachment(attachment) }
                        )
                    }
                }
            }

            // Message body
            OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text(stringResource(R.string.message)) },
                    modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp),
                    maxLines = 10
            )
        }
    }
}

@Composable
fun AttachmentThumbnail(uri: String, onRemove: () -> Unit) {
    Box(modifier = Modifier.size(100.dp)) {
        AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
        )
        IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.TopEnd)) {
            Icon(Icons.Default.Close, contentDescription = stringResource(R.string.remove))
        }
    }
}
