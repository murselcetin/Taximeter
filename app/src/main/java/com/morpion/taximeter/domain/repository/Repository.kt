package com.morpion.taximeter.domain.repository

import com.morpion.taximeter.data.remote.entity.response.GetTaxiStandsListResponse
import com.morpion.taximeter.util.RestResult

interface Repository {
    suspend fun getTaximeterStands(): RestResult<GetTaxiStandsListResponse>
}