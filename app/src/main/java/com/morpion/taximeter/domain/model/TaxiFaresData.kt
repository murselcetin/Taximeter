package com.morpion.taximeter.domain.model

import com.google.gson.annotations.SerializedName

data class TaxiFaresData(
    var id: String? = null,
    var city: String? = null,
    var startFee: String? = null,
    var feePerKm: String? = null,
    var defaultFee: String? = null,
    var info: String? = null
)
