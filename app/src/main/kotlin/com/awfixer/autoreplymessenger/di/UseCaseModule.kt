package com.awfixer.autoreplymessenger.di

import com.awfixer.autoreplymessenger.data.repository.MessageRepository
import com.awfixer.autoreplymessenger.data.repository.MmsRepository
import com.awfixer.autoreplymessenger.data.repository.ThreadRepository
import com.awfixer.autoreplymessenger.domain.usecase.*
import com.awfixer.autoreplymessenger.utils.ApnResolver
import com.awfixer.autoreplymessenger.utils.MmsHttpClient
import com.awfixer.autoreplymessenger.utils.MmsPduParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideReceiveMmsUseCase(
            mmsRepository: MmsRepository,
            parseMmsPduUseCase: ParseMmsPduUseCase,
            ingestMmsIntoThreadUseCase: IngestMmsIntoThreadUseCase
    ): ReceiveMmsUseCase {
        return ReceiveMmsUseCase(mmsRepository, parseMmsPduUseCase, ingestMmsIntoThreadUseCase)
    }

    @Provides
    @Singleton
    fun provideParseMmsPduUseCase(mmsPduParser: MmsPduParser): ParseMmsPduUseCase {
        return ParseMmsPduUseCase(mmsPduParser)
    }

    @Provides
    @Singleton
    fun provideIngestMmsIntoThreadUseCase(
            messageRepository: MessageRepository,
            threadRepository: ThreadRepository
    ): IngestMmsIntoThreadUseCase {
        return IngestMmsIntoThreadUseCase(messageRepository, threadRepository)
    }

    @Provides
    @Singleton
    fun provideSendMmsUseCase(
            apnResolver: ApnResolver,
            mmsHttpClient: MmsHttpClient,
            messageRepository: MessageRepository
    ): SendMmsUseCase {
        return SendMmsUseCase(apnResolver, mmsHttpClient, messageRepository)
    }
}
