package com.morpion.taximeter.common.extensions

import java.text.SimpleDateFormat
import java.util.*


fun String.toPhoneFormat(): String {
    return "0 ${this.subSequence(0, 3)} ${this.subSequence(3, 6)} ${this.subSequence(6, 8)} ${this.subSequence(8, 10)}"
}

fun String.kmToDouble(): String{
    return this.dropLast(3).replace(',', '.')
}