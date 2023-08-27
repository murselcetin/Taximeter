package com.morpion.taximeter.data.repository

import com.morpion.taximeter.data.remote.ApiService
import com.morpion.taximeter.data.remote.entity.response.GetTaxiStandsListResponse
import com.morpion.taximeter.domain.repository.Repository
import com.morpion.taximeter.util.RequestSafe
import com.morpion.taximeter.util.RestResult
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService) : Repository, RequestSafe {

    override suspend fun getTaxiStands() = safeApiCall { api.getTaxiStands() }
    override suspend fun getTaxiFares() = safeApiCall { api.getTaxiFares() }

}