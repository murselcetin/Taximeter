package com.morpion.taximeter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData

@Database(entities = [TaximeterHistoryLocalData::class], version = 1, exportSchema = false)
abstract class RoomDb : RoomDatabase() {
    abstract fun getRoomDao(): RoomDao
}