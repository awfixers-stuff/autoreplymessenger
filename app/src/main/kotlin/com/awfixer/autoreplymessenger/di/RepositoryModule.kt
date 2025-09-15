package com.awfixer.autoreplymessenger.di

import android.content.Context
import com.awfixer.autoreplymessenger.data.dao.MessageDao
import com.awfixer.autoreplymessenger.data.dao.ThreadDao
import com.awfixer.autoreplymessenger.data.repository.AutoReplyRepository
import com.awfixer.autoreplymessenger.data.repository.MessageRepository
import com.awfixer.autoreplymessenger.data.repository.MmsRepository
import com.awfixer.autoreplymessenger.data.repository.ThreadRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMessageRepository(messageDao: MessageDao, threadDao: ThreadDao): MessageRepository {
        return MessageRepository(messageDao, threadDao)
    }

    @Provides
    @Singleton
    fun provideThreadRepository(threadDao: ThreadDao): ThreadRepository {
        return ThreadRepository(threadDao)
    }

    @Provides
    @Singleton
    fun provideMmsRepository(messageDao: MessageDao): MmsRepository {
        return MmsRepository(messageDao)
    }

    @Provides
    @Singleton
    fun provideAutoReplyRepository(
            @ApplicationContext context: Context,
            messageRepository: MessageRepository
    ): AutoReplyRepository {
        return AutoReplyRepository(context, messageRepository)
    }
}
