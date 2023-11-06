package com.morpion.taximeter.di

import android.content.Context
import android.content.SharedPreferences
import com.morpion.taximeter.data.local.RoomDb
import com.morpion.taximeter.shared.TaxiFaresManager
import com.morpion.taximeter.shared.TaxiStandsManager
import com.morpion.taximeter.util.DirectionsJSONParser
import com.morpion.taximeter.util.DownloadTask
import com.morpion.taximeter.util.LocalSessions
import com.morpion.taximeter.util.ParserTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun mySharedPreferences(@ApplicationContext app: Context): SharedPreferences {
        return app.getSharedPreferences(
            "local_sessions",
            Context.MODE_PRIVATE
        )
    }

    @Singleton
    @Provides
    fun provideMySharedPreferences(sharedPreferences: SharedPreferences):LocalSessions = LocalSessions(sharedPreferences)

    @Singleton
    @Provides
    fun provideTaxiStandsManager(local: LocalSessions): TaxiStandsManager = TaxiStandsManager(local)

    @Singleton
    @Provides
    fun provideTaxiFaresManager(local: LocalSessions): TaxiFaresManager = TaxiFaresManager(local)

    @Provides
    @Singleton
    fun provideRoomDao(db: RoomDb) = db.getRoomDao()


    @Provides
    @Singleton
    fun provideDirectionsJsonParser(local: LocalSessions) = DirectionsJSONParser(local)

    @Provides
    @Singleton
    fun provideParserTask(local: DirectionsJSONParser) = ParserTask(local)

    @Provides
    @Singleton
    fun provideDownloadTask(local: ParserTask) = DownloadTask(local)



}