package com.awfixer.autoreplymessenger.di

import android.content.Context
import com.awfixer.autoreplymessenger.utils.ApnResolver
import com.awfixer.autoreplymessenger.utils.MmsHttpClient
import com.awfixer.autoreplymessenger.utils.MmsPduParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilsModule {

    @Provides
    @Singleton
    fun provideMmsPduParser(@ApplicationContext context: Context): MmsPduParser {
        return MmsPduParser(context)
    }

    @Provides
    @Singleton
    fun provideApnResolver(@ApplicationContext context: Context): ApnResolver {
        return ApnResolver(context)
    }

    @Provides
    @Singleton
    fun provideMmsHttpClient(@ApplicationContext context: Context): MmsHttpClient {
        return MmsHttpClient(context)
    }
}
