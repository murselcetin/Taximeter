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

    @Query("SELECT * FROM taximeter_history ORDER BY id DESC ")
    fun getTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>>

    @Query("SELECT * FROM taximeter_history ORDER BY id DESC LIMIT 10")
    fun getLastTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>>

    @Query("DELETE FROM taximeter_history WHERE id = :id")
    suspend fun deleteTaximeter(id: Int)
}