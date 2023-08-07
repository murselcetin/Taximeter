package com.morpion.taximeter.di

import android.content.Context
import androidx.room.Room
import com.morpion.taximeter.data.local.RoomDao
import com.morpion.taximeter.data.local.RoomDb
import com.morpion.taximeter.data.repository.LocalRepositoryImpl
import com.morpion.taximeter.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDb::class.java, "taxi").build()

    @Provides
    @Singleton
    fun provideLocalRepository(dao: RoomDao): LocalRepository {
        return LocalRepositoryImpl(dao)
    }
}