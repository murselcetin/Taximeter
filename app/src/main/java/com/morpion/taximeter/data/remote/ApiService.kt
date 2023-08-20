package com.morpion.taximeter.data.remote

import com.morpion.taximeter.data.remote.entity.response.GetTaxiStandsListResponse
import com.morpion.taximeter.presentation.base.BaseResponse
import retrofit2.Response
import retrofit2.http.POST

interface ApiService {

    @POST("api/get_taxi_stands.php")
    suspend fun getTaxiStands(): Response<BaseResponse<GetTaxiStandsListResponse>>

}