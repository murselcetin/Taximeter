package com.morpion.taximeter.di

import android.content.Context
import androidx.room.Room
import com.morpion.taximeter.data.local.RoomDao
import com.morpion.taximeter.data.local.RoomDb
import com.morpion.taximeter.data.remote.ApiService
import com.morpion.taximeter.data.repository.LocalRepositoryImpl
import com.morpion.taximeter.data.repository.RepositoryImpl
import com.morpion.taximeter.domain.repository.LocalRepository
import com.morpion.taximeter.domain.repository.Repository
import com.morpion.taximeter.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRepository(api: ApiService): Repository {
        return RepositoryImpl(api)
    }

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