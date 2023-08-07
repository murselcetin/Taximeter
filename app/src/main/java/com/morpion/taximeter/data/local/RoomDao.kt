package com.morpion.taximeter.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Insert
    suspend fun saveTaximeter(taximeterHistoryData: TaximeterHistoryLocalData)

    @Query("SELECT * FROM taximeter_history")
    fun getTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>>

    @Query("SELECT * FROM taximeter_history ORDER BY id LIMIT 10")
    fun getLastTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>>

}