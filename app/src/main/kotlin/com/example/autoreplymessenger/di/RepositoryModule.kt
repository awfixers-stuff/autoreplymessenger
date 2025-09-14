<file_path>
autoreplyapp/app/src/main/kotlin/com/example/autoreplymessenger/di/RepositoryModule.kt
</file_path>

<edit_description>
Create RepositoryModule.kt for Hilt
</edit_description>

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
