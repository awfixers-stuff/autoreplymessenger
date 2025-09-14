package com.example.autoreplymessenger.di

import android.content.Context
import androidx.room.Room
import com.example.autoreplymessenger.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "autoreply_messenger.db"
        ).build()
    }

    @Provides
    fun provideMessageDao(database: AppDatabase) = database.messageDao()

    @Provides
    fun provideConversationDao(database: AppDatabase) = database.conversationDao()
}
```

```kotlin
package com.example.autoreplymessenger.di

import com.example.autoreplymessenger.data.dao.ConversationDao
import com.example.autoreplymessenger.data.dao.MessageDao
import com.example.autoreplymessenger.data.repository.ConversationRepository
import com.example.autoreplymessenger.data.repository.MessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMessageRepository(
            messageDao: MessageDao,
            conversationDao: ConversationDao
    ): MessageRepository {
        return MessageRepository(messageDao, conversationDao)
    }

    @Provides
    @Singleton
    fun provideConversationRepository(conversationDao: ConversationDao): ConversationRepository {
        return ConversationRepository(conversationDao)
    }
}
```

```kotlin
package com.example.autoreplymessenger.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import com.example.autoreplymessenger.data.model.Message
import com.example.autoreplymessenger.data.repository.MessageRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject lateinit var messageRepository: MessageRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (smsMessage in messages) {
                val message = Message(
                        threadId = smsMessage.threadId,
                        sender = smsMessage.originatingAddress ?: "",
                        body = smsMessage.messageBody,
                        timestamp = smsMessage.timestampMillis,
                        type = 1, // SMS
                        isIncoming = true,
                        isRead = false
                )
                CoroutineScope(Dispatchers.IO).launch {
                    messageRepository.insertMessage(message)
                }
            }
        }
    }
}
```

```kotlin
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
```

```kotlin
package com.example.autoreplymessenger.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.autoreplymessenger.data.model.Conversation
import com.example.autoreplymessenger.ui.viewmodels.InboxViewModel

@Composable
fun InboxScreen(navController: NavController) {
    val viewModel: InboxViewModel = hiltViewModel()
    val conversations by viewModel.conversations.collectAsState()

    LazyColumn {
        items(conversations) { conversation ->
            ConversationItem(conversation = conversation)
        }
    }
}

@Composable
fun ConversationItem(conversation: Conversation) {
    Text(text = "${conversation.contactNumber}: ${conversation.snippet}")
}
```

```kotlin
package com.example.autoreplymessenger

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AutoReplyMessengerApplication : Application()
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.example.autoreplymessenger">

    <!-- Permissions for SMS/MMS -->
    <uses-permission android:name="android.permission.RECEIVE_SMS" />
    <uses-permission android:name="android.permission.SEND_SMS" />
    <uses-permission android:name="android.permission.READ_SMS" />
    <uses-permission android:name="android.permission.REQUEST_SMS" />

    <!-- Permissions for Phone Calls -->
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.CALL_PHONE" />
    <uses-permission android:name="android.permission.ANSWER_PHONE_CALLS" />
    <uses-permission android:name="android.permission.READ_CALL_LOG" />
    <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS" />

    <!-- Permissions for Contacts -->
    <uses-permission android:name="android.permission.READ_CONTACTS" />

    <!-- Permissions for Storage (for MMS attachments, backups) -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

    <!-- Permissions for Network (for MMS sending) -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <!-- Permissions for Wake Lock (for background tasks) -->
    <uses-permission android:name="android.permission.WAKE_LOCK" />

    <!-- Permissions for Notifications (for Do Not Disturb) -->
    <uses-permission android:name="android.permission.ACCESS_NOTIFICATION_POLICY" />

    <application
        android:name=".AutoReplyMessengerApplication"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.AutoReplyMessenger"
        android:usesCleartextTraffic="false"
        tools:targetApi="31">

        <activity
            android:name=".ui.MainActivity"
            android:exported="true"
            android:theme="@style/Theme.AutoReplyMessenger">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- SMS Receiver -->
        <receiver
            android:name=".services.SmsReceiver"
            android:exported="true"
            android:permission="android.permission.BROADCAST_SMS">
            <intent-filter android:priority="1000">
                <action android:name="android.provider.Telephony.SMS_RECEIVED" />
            </intent-filter>
        </receiver>

        <!-- MMS Receiver -->
        <receiver
            android:name=".services.MmsReceiver"
            android:exported="true">
            <intent-filter android:priority="1000">
                <action android:name="android.provider.Telephony.WAP_PUSH_RECEIVED" />
                <data android:mimeType="application/vnd.wap.mms-message" />
            </intent-filter>
        </receiver>

        <!-- Call Receiver -->
        <receiver
            android:name=".services.CallReceiver"
            android:exported="true">
            <intent-filter android:priority="1000">
                <action android:name="android.intent.action.PHONE_STATE" />
                <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
            </intent-filter>
        </receiver>

        <!-- Call Screening Service -->
        <service
            android:name=".services.CallScreeningService"
            android:permission="android.permission.BIND_SCREENING_SERVICE"
            android:exported="true">
            <intent-filter>
                <action android:name="android.telecom.CallScreeningService" />
            </intent-filter>
        </service>

        <!-- In Call Service -->
        <service
            android:name=".services.InCallService"
            android:permission="android.permission.BIND_INCALL_SERVICE"
            android:exported="true">
            <intent-filter>
                <action android:name="android.telecom.InCallService" />
            </intent-filter>
        </service>

        <!-- Work Manager -->
        <provider
            android:name="androidx.startup.InitializationProvider"
            android:authorities="${applicationId}.androidx-startup"
            android:exported="false"
            tools:node="merge">
            <meta-data
                android:name="androidx.work.WorkManagerInitializer"
                android:value="androidx.startup"
                tools:node="remove" />
        </provider>

    </application>

</manifest>
```

```md
# AutoReplyMessenger

An open-source Android application that serves as a full-featured SMS, MMS, and Phone app with advanced automation for replying to incoming messages and calls.

## Features

- **SMS/MMS Handling**: Receive, send, and manage SMS and MMS messages.
- **Phone Call Handling**: Handle incoming and outgoing calls with auto-reply features.
- **Automation**: Set up custom auto-replies based on conditions.
- **Privacy-Focused**: Local-only, no cloud dependencies.
- **Material Design 3**: Modern UI with Compose.

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/AutoReplyMessenger.git
   cd AutoReplyMessenger
   ```

2. Open the project in Android Studio.

3. Build and run the app on an emulator or device (API 29+).

## Requirements

- Android Studio Arctic Fox or later
- Kotlin 1.9.10
- Android SDK API 35

## Architecture

- **MVVM** with Jetpack components
- **Room** for local database
- **Hilt** for dependency injection
- **Compose** for UI

## Contributing

Contributions are welcome! Please read the contribution guidelines.

## License

MIT License
```

```md
# Changelog

## [1.0.0] - 2023-10-01

### Added
- Core SMS handling
- Basic UI with Compose
- Room database for messages and conversations
- Hilt dependency injection

### Iteration 1: Core SMS handling and basic UI
- Implemented SMS receiving via BroadcastReceiver
- Basic inbox screen with conversation list
- Data models and repositories
```

```text
MIT License

Copyright (c) 2023 Your Name

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

This completes Iteration 1 of the AutoReplyMessenger app. The code includes core SMS handling, basic UI, data models, repositories, and dependency injection setup. For future iterations, we can expand on MMS, calls, automation, and advanced features as outlined in the prompt.
