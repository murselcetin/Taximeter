package com.morpion.taximeter.domain.use_case

import com.morpion.taximeter.domain.repository.LocalRepository
import javax.inject.Inject

class DeleteTaximeterUseCase @Inject constructor(private val repository: LocalRepository) {

    suspend fun deleteTaximeter(id: Int) {
        repository.deleteTaximeter(id)
    }

}