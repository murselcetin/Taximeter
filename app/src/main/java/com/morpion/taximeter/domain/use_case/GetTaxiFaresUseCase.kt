package com.morpion.taximeter.domain.use_case

import com.morpion.taximeter.data.remote.entity.response.toTaxiFaresList
import com.morpion.taximeter.domain.model.TaxiFaresData
import com.morpion.taximeter.domain.repository.Repository
import com.morpion.taximeter.util.RestResult
import com.morpion.taximeter.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTaxiFaresUseCase @Inject constructor(private val repository: Repository) :
    NoInputUseCaseWithFlow<RestResult<List<TaxiFaresData>>> {

    override suspend fun invoke(): Flow<RestResult<List<TaxiFaresData>>> =
        flow {
            emit(
                repository.getTaxiFares().map {
                    it.taxiFaresList.toTaxiFaresList()
                }
            )
        }

}