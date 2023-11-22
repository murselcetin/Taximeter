package com.morpion.taximeter.common.extensions

import java.text.SimpleDateFormat
import java.util.*

fun Long.timestampToDate(): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat("dd.MM.yyyy")
    return dateFormat.format(date)
}