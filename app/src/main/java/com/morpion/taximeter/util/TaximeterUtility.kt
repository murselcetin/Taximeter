package com.morpion.taximeter.util

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object TaximeterUtility {

    // GPS Lokasyon izinleri alma
    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }

    fun getFormattedStopwatchTime(sec: Long): String {
        val hours = sec /3600
        val minutes = sec /60%60
        val seconds = sec %60

        return "${if(hours < 10) "0" else ""}$hours:" +
                "${if(minutes < 10) "0" else ""}$minutes:" +
                "${if(seconds < 10) "0" else ""}$seconds"

    }

    // Tek rota ölçümü
    fun calculatePolylineLength(polyline: Polyline): Float {
        var distance = 0f
        for(i in 0..polyline.size-2) {
            val result = FloatArray(1)
            val pos1 = polyline[i]
            val pos2 = polyline[i+1]
            Location.distanceBetween(pos1.latitude,pos1.longitude,pos2.latitude,pos2.longitude,result)
            distance += result[0]
        }
        return distance
    }

    // Duraklatma olan rota mesafe ölçümü
    fun calculateLengthofPolylines(polylines: Polylines): Float {
        var totalDistance = 0f
        for(i in 0 until polylines.size) {
            totalDistance += calculatePolylineLength(polylines[i])
        }
        return totalDistance
    }
}