package com.morpion.taximeter.util

import android.content.SharedPreferences
import javax.inject.Inject

class LocalSessions @Inject constructor(var prefs: SharedPreferences)  {

    var taximeterStartPrice:Float
        get() = prefs.getFloat(Constants.TAXIMETER_START_PRICE,0f)
        set(value) = prefs.edit().putFloat(Constants.TAXIMETER_START_PRICE,value).apply()

    var taximeterKmPrice: Float
        get() = prefs.getFloat(Constants.TAXIMETER_KM_PRICE, 0f)
        set(value) = prefs.edit().putFloat(Constants.TAXIMETER_KM_PRICE, value).apply()
}