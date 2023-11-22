package com.morpion.taximeter.data.remote.entity.response

import com.google.gson.annotations.SerializedName
import com.morpion.taximeter.domain.model.TaxiStandsData

data class GetTaxiStandsListResponse(
    @SerializedName("taxi_stands_list")
    val taxiStandsList: List<GetTaxiStandsResponse>
)

data class GetTaxiStandsResponse(
    @SerializedName("id") var id: String? = null,
    @SerializedName("durakAd") var durakAd: String? = null,
    @SerializedName("durakAdres") var durakAdres: String? = null,
    @SerializedName("durakTel") var durakTel: String? = null,
    @SerializedName("Sehir") var sehir: String? = null,
    @SerializedName("ilce") var ilce: String? = null,
    @SerializedName("latitude") var latitude: String? = null,
    @SerializedName("longitude") var longitude: String? = null
)

fun List<GetTaxiStandsResponse>.toTaxiStandsList() = map {
    TaxiStandsData(
        id = it.id,
        durakAd = it.durakAd,
        durakAdres = it.durakAdres,
        durakTel = it.durakTel,
        sehir = it.sehir,
        ilce = it.ilce,
        latitude = it.latitude,
        longitude = it.longitude
    )
}