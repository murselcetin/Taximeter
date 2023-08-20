package com.morpion.taximeter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.util.Converters

@Database(entities = [TaximeterHistoryLocalData::class],version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RoomDb : RoomDatabase() {
    abstract fun getRoomDao(): RoomDao
}