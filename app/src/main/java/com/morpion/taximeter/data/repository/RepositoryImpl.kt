package com.morpion.taximeter.data.repository

import com.morpion.taximeter.data.remote.ApiService
import com.morpion.taximeter.domain.repository.Repository
import com.morpion.taximeter.util.RequestSafe
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val api: ApiService) : Repository, RequestSafe {

    override suspend fun getTaximeterStands() = safeApiCall { api.getTaxiStands() }

}