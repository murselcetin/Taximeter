package com.morpion.taximeter.domain.repository

import com.morpion.taximeter.data.remote.entity.response.GetTaxiFaresListResponse
import com.morpion.taximeter.data.remote.entity.response.GetTaxiStandsListResponse
import com.morpion.taximeter.util.RestResult

interface Repository {
    suspend fun getTaxiStands(): RestResult<GetTaxiStandsListResponse>
    suspend fun getTaxiFares(): RestResult<GetTaxiFaresListResponse>
}