package com.morpion.taximeter.util

import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import com.morpion.taximeter.databinding.FragmentDirectionsBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class DirectionsJSONParser {

    /** Google Directions URL kullanarak web servisden elde edilen JSONObject çözümlenerek,
     * rotayı ne kadar sürede bitirebileceğiniz, rotanın kaç km olduğu, yol tarifleri hakkında detaylı bilgiler elde edilir    */
    fun parse(jObject: JSONObject): List<List<HashMap<String, String>>>{
        val routes: MutableList<List<HashMap<String, String>>> = ArrayList()
        var jRoutes: JSONArray? = null
        var jLegs: JSONArray? = null
        var jSteps: JSONArray? = null
        try {
            jRoutes = jObject.getJSONArray("routes")
            for (i in 0 until jRoutes.length()) {
                jLegs = (jRoutes[i] as JSONObject).getJSONArray("legs")
                val c = jLegs.getJSONObject(i)

                val dist = c.getJSONObject("distance")
                val dur = c.getJSONObject("duration")

                Log.e("TAG", "${dist.getString("text")} - ${dur.getString("text")}", )

                val path = ArrayList<HashMap<String, String>>()
                for (j in 0 until jLegs.length()) {
                    jSteps = (jLegs[j] as JSONObject).getJSONArray("steps")
                    for (k in 0 until jSteps.length()) {
                        //Polyline çözümleyerek, rota noktalarının enlem, boylam bilgisi alma
                        var polyline = ""
                        polyline =
                            ((jSteps[k] as JSONObject)["polyline"] as JSONObject)["points"] as String
                        val list = decodePoly(polyline)
                        for (l in list.indices) {
                            val hm = HashMap<String, String>()
                            hm["lat"] = java.lang.Double.toString(list[l].latitude)
                            hm["lng"] = java.lang.Double.toString(list[l].longitude)
                            path.add(hm)
                        }
                    }
                    routes.add(path)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: Exception) {
        }
        return routes
    }

    /**
     * Polyline (rota yolunun şifrelenmiş çizgileri) çözümleyerek, rota noktalarının enlem, boylam bilgisini döner
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }
}