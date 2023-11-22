package com.morpion.taximeter.domain.use_case

import com.morpion.taximeter.data.remote.entity.response.toTaxiStandsList
import com.morpion.taximeter.domain.model.TaxiStandsData
import com.morpion.taximeter.domain.repository.Repository
import com.morpion.taximeter.util.RestResult
import com.morpion.taximeter.util.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTaxiStandsUseCase @Inject constructor(private val repository: Repository) :
    NoInputUseCaseWithFlow<RestResult<List<TaxiStandsData>>> {

    override suspend fun invoke(): Flow<RestResult<List<TaxiStandsData>>> =
        flow {
            emit(
                repository.getTaxiStands().map {
                    it.taxiStandsList.toTaxiStandsList()
                }
            )
        }

}