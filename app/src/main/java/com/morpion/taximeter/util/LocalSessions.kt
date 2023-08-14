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

    var duration: String?
        get() = prefs.getString(Constants.DURATION, null)
        set(value) = prefs.edit().putString(Constants.DURATION, value).apply()

    var distance: String?
        get() = prefs.getString(Constants.DISTANCE, null)
        set(value) = prefs.edit().putString(Constants.DISTANCE, value).apply()

}