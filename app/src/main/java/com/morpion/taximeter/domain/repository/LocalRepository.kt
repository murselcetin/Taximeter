package com.morpion.taximeter.domain.repository

import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun saveTaximeter(taximeterHistoryData: TaximeterHistoryLocalData)
    fun getTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>>
    fun getLastTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>>
    suspend fun deleteTaximeter(id: Int)
}