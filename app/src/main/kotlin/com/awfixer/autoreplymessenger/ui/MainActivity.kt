package com.awfixer.autoreplymessenger.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.awfixer.autoreplymessenger.ui.composables.ComposeScreen
import com.awfixer.autoreplymessenger.ui.composables.InboxScreen
import com.awfixer.autoreplymessenger.ui.theme.AutoReplyMessengerTheme
import com.awfixer.autoreplymessenger.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutoReplyMessengerTheme {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) { AppNavigation() }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "inbox") {
        composable("inbox") { InboxScreen(navController) }
        composable("compose") {
            com.awfixer.autoreplymessenger.ui.composables.ComposeScreen(navController)
        }
        composable("dialer") { DialerScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}

// Placeholder composables for screens

@Composable
fun DialerScreen(navController: androidx.navigation.NavController) {
    val context = LocalContext.current
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Dialer") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                )
            },
            content = { padding ->
                Column(
                        modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                            value = phoneNumber,
                            onValueChange = { phoneNumber = it },
                            label = { Text("Phone Number") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true
                    )
                    // Dial pad buttons
                    val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
                    LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(8.dp)
                    ) {
                        items(digits) { digit ->
                            Button(
                                    onClick = {
                                        phoneNumber = TextFieldValue(phoneNumber.text + digit)
                                    },
                                    modifier = Modifier.aspectRatio(1f).padding(4.dp)
                            ) { Text(digit, style = MaterialTheme.typography.headlineMedium) }
                        }
                    }
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                                onClick = {
                                    if (phoneNumber.text.isNotEmpty()) {
                                        phoneNumber = TextFieldValue(phoneNumber.text.dropLast(1))
                                    }
                                },
                                modifier = Modifier.weight(1f)
                        ) { Text("Delete") }
                        Button(
                                onClick = {
                                    if (phoneNumber.text.isNotEmpty()) {
                                        val intent =
                                                Intent(
                                                        Intent.ACTION_CALL,
                                                        Uri.parse("tel:${phoneNumber.text}")
                                                )
                                        context.startActivity(intent)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                        ) { Text("Call") }
                    }
                }
            }
    )
}

@Composable
fun SettingsScreen(navController: androidx.navigation.NavController) {
    val viewModel: SettingsViewModel = hiltViewModel()
    val autoReplyEnabled by viewModel.autoReplyEnabled.collectAsState()
    val replyTemplate by viewModel.replyTemplate.collectAsState()
    var templateText by remember { mutableStateOf(TextFieldValue(replyTemplate)) }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = { Text("Settings") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                )
            },
            content = { padding ->
                Column(
                        modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Auto-reply enabled", modifier = Modifier.weight(1f))
                        Switch(
                                checked = autoReplyEnabled,
                                onCheckedChange = { viewModel.setAutoReplyEnabled(it) }
                        )
                    }
                    OutlinedTextField(
                            value = templateText,
                            onValueChange = {
                                templateText = it
                                viewModel.setReplyTemplate(it.text)
                            },
                            label = { Text("Reply Template") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 3
                    )
                    Text("Use {alternative_platform} and {new_number} as placeholders.")
                }
            }
    )
}
