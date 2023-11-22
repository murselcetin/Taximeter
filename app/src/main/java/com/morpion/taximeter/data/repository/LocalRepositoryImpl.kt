package com.morpion.taximeter.data.repository

import com.morpion.taximeter.data.local.RoomDao
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val dao: RoomDao) : LocalRepository {

    override suspend fun saveTaximeter(taximeterHistoryData: TaximeterHistoryLocalData) {
        dao.saveTaximeter(taximeterHistoryData)
    }

    override fun getTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>> {
        return dao.getTaximeterHistory()
    }

    override fun getLastTaximeterHistory(): Flow<List<TaximeterHistoryLocalData>> {
        return dao.getLastTaximeterHistory()
    }

    override suspend fun deleteTaximeter(id: Int) {
        dao.deleteTaximeter(id)
    }

}