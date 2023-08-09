package com.morpion.taximeter.domain.model.ui

import android.graphics.Bitmap

data class TaximeterHistoryUIModel(
    val id: Int,
    val paid: String,
    val distance: String,
    val time: String,
    val date: Long,
    val img: Bitmap?
)
