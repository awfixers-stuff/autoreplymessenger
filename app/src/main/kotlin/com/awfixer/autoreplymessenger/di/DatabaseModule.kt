package com.awfixer.autoreplymessenger.di

import android.content.Context
import androidx.room.Room
import com.awfixer.autoreplymessenger.data.AppDatabase
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
                )
                .build()
    }

    @Provides fun provideMessageDao(database: AppDatabase) = database.messageDao()

    @Provides fun provideConversationDao(database: AppDatabase) = database.conversationDao()
}
