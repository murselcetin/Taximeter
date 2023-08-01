package com.morpion.taximeter.presentation.base

import com.google.gson.annotations.SerializedName

data class BaseResponse<out T>(
    @SerializedName("success") var success: Boolean,
    @SerializedName("result") val data: T
)
