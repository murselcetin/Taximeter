package com.morpion.taximeter.domain.model.ui

data class TaximeterHistoryUIModel(
    val id: Int,
    val paid: String,
    val distance: String,
    val time: String,
    val date: String
)
