package com.morpion.taximeter.data.remote.entity.response

import com.google.gson.annotations.SerializedName
import com.morpion.taximeter.domain.model.TaxiFaresData

data class GetTaxiFaresListResponse(
    @SerializedName("taxi_fares_list")
    val taxiFaresList: List<GetTaxiFaresResponse>
)

data class GetTaxiFaresResponse(
    @SerializedName("id") var id: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("start_fee") var startFee: String? = null,
    @SerializedName("fee_per_km") var feePerKm: String? = null,
    @SerializedName("default_fee") var defaultFee: String? = null,
    @SerializedName("info") var info: String? = null
)

fun List<GetTaxiFaresResponse>.toTaxiFaresList() = map {
    TaxiFaresData(
        id = it.id,
        city = it.city,
        startFee = it.startFee,
        feePerKm = it.feePerKm,
        defaultFee = it.defaultFee,
        info = it.info
    )
}