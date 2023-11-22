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

    var firstTutorial: Boolean
        get() = prefs.getBoolean(Constants.FIRST_TUTORIAL, false)
        set(value) = prefs.edit().putBoolean(Constants.FIRST_TUTORIAL, value).apply()

    var firstTaximeterFee: Boolean
        get() = prefs.getBoolean(Constants.FIRST_TAXIMETER_FEE, false)
        set(value) = prefs.edit().putBoolean(Constants.FIRST_TAXIMETER_FEE, value).apply()

    var duration: String?
        get() = prefs.getString(Constants.DURATION, "0.0")
        set(value) = prefs.edit().putString(Constants.DURATION, value).apply()

    var distance: String?
        get() = prefs.getString(Constants.DISTANCE, "0.0")
        set(value) = prefs.edit().putString(Constants.DISTANCE, value).apply()

}