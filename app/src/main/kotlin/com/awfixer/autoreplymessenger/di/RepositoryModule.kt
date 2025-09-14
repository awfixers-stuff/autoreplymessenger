<file_path>
autoreplyapp/app/src/main/kotlin/com/awfixer/autoreplymessenger/di/RepositoryModule.kt
</file_path>

<edit_description>
Update package and imports in RepositoryModule.kt
</edit_description>

package com.awfixer.autoreplymessenger.di

import com.awfixer.autoreplymessenger.data.dao.ConversationDao
import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.repository.ConversationRepository
import com.awfixer.autoreplymessenger.data.repository.MessageRepository
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
