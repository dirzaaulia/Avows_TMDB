package com.dirzaaulia.avowstmdb.di

import android.content.Context
import com.dirzaaulia.avowstmdb.network.Service
import com.dirzaaulia.avowstmdb.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideService(@ApplicationContext context: Context): Service {
        return Service.create(context)
    }
}