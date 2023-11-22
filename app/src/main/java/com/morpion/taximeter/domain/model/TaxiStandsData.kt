package com.morpion.taximeter.domain.model

data class TaxiStandsData(
    var id: String? = null,
    var durakAd: String? = null,
    var durakAdres: String? = null,
    var durakTel: String? = null,
    var sehir: String? = null,
    var ilce: String? = null,
    var latitude: String? = null,
    var longitude: String? = null
)