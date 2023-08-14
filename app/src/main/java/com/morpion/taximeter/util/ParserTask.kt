package com.morpion.taximeter.util

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.morpion.taximeter.databinding.FragmentDirectionsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class ParserTask() {
    suspend fun execute(jsonData: String, mMap: GoogleMap) = coroutineScope {
        val routes = parseDirections(jsonData)
        drawPolyline(routes,mMap)
    }

    private suspend fun parseDirections(jsonData: String): List<List<HashMap<String, String>>> = withContext(
        Dispatchers.Default) {
        val jObject = JSONObject(jsonData)
        val parser = DirectionsJSONParser()
        return@withContext parser.parse(jObject)
    }

    private suspend fun drawPolyline(routes: List<List<HashMap<String, String>>>,mMap: GoogleMap) = withContext(
        Dispatchers.Main) {
        var points: ArrayList<LatLng>? = null
        var lineOptions: PolylineOptions? = null

        for (path in routes) {
            points = ArrayList()
            lineOptions = PolylineOptions()

            for (point in path) {
                val lat = point["lat"]?.toDoubleOrNull() ?: continue
                val lng = point["lng"]?.toDoubleOrNull() ?: continue
                val position = LatLng(lat, lng)
                points.add(position)
            }

            lineOptions.addAll(points)
            lineOptions.width(8f)
            lineOptions.color(Color.RED)
        }

        if (lineOptions != null) {
            mMap.addPolyline(lineOptions)
        } else {
            Log.e("TAG", "rota bulunamadı")
        }
    }
}