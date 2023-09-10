package com.morpion.taximeter.util

import android.content.SharedPreferences
import javax.inject.Inject

class LocalSessions @Inject constructor(var prefs: SharedPreferences) {

    var taximeterStartPrice: String?
        get() = prefs.getString(Constants.TAXIMETER_START_PRICE, "0.0")
        set(value) = prefs.edit().putString(Constants.TAXIMETER_START_PRICE, value).apply()

    var taximeterKmPrice: String?
        get() = prefs.getString(Constants.TAXIMETER_KM_PRICE, "0.0")
        set(value) = prefs.edit().putString(Constants.TAXIMETER_KM_PRICE, value).apply()

    var taxiStandsList: String?
        get() = prefs.getString(Constants.TAXI_STANDS_LIST, null)
        set(value) = prefs.edit().putString(Constants.TAXI_STANDS_LIST, value).apply()

    var taxiFaresList: String?
        get() = prefs.getString(Constants.TAXI_FARES_LIST, null)
        set(value) = prefs.edit().putString(Constants.TAXI_FARES_LIST, value).apply()

}