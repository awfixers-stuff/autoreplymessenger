package com.example.autoreplymessenger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.autoreplymessenger.ui.composables.InboxScreen
import com.example.autoreplymessenger.ui.theme.AutoReplyMessengerTheme
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
        composable("compose") { ComposeScreen(navController) }
        composable("dialer") { DialerScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}

// Placeholder composables for screens
@Composable
fun ComposeScreen(navController: androidx.navigation.NavController) {
    // TODO: Implement Compose Screen
}

@Composable
fun DialerScreen(navController: androidx.navigation.NavController) {
    // TODO: Implement Dialer Screen
}

@Composable
fun SettingsScreen(navController: androidx.navigation.NavController) {
    // TODO: Implement Settings Screen
}
