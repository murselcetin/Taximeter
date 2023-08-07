package com.morpion.taximeter.domain.use_case

import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.domain.repository.LocalRepository
import javax.inject.Inject

class SaveTaximeterUseCase @Inject constructor(private val repository: LocalRepository) {

    suspend fun saveTaximeter(input: TaximeterHistoryLocalData) {
        repository.saveTaximeter(input)
    }

}