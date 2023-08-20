package com.morpion.taximeter.util

import android.graphics.Color

object Constants {

    const val BASE_URL = "https://morpionsoftware.com/taximeter/"

    const val TAXI_STANDS_LIST = "taxi_stands_list"

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    const val ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY"

    const val NOTIFICATION_ID = 1
    const val NOTIFICATION_CHANNEL_ID = "1"
    const val NOTIFICATION_CHANNEL_NAME = "Taximeter"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val POLYLINE_COLOR = Color.RED
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val TAXIMETER_START_PRICE = "taximeter_start_price"
    const val TAXIMETER_KM_PRICE = "taximeter_km_price"

    const val DURATION = "duration"
    const val DISTANCE = "distance"

}