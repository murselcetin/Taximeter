package com.morpion.taximeter.util

import android.content.SharedPreferences
import javax.inject.Inject

class LocalSessions @Inject constructor(var prefs: SharedPreferences) {

    var taximeterStartPrice: Float
        get() = prefs.getFloat(Constants.TAXIMETER_START_PRICE, 0f)
        set(value) = prefs.edit().putFloat(Constants.TAXIMETER_START_PRICE, value).apply()

    var taximeterKmPrice: Float
        get() = prefs.getFloat(Constants.TAXIMETER_KM_PRICE, 0f)
        set(value) = prefs.edit().putFloat(Constants.TAXIMETER_KM_PRICE, value).apply()

    var taxiStandsList: String?
        get() = prefs.getString(Constants.TAXI_STANDS_LIST, null)
        set(value) = prefs.edit().putString(Constants.TAXI_STANDS_LIST, value).apply()

}