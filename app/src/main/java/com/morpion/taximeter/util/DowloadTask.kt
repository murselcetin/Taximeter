package com.morpion.taximeter.util

import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.morpion.taximeter.databinding.FragmentDirectionsBinding
import com.morpion.taximeter.presentation.ui.DirectionsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class DownloadTask {
    suspend fun execute(url: String,mMap: GoogleMap) = coroutineScope {
        launch(Dispatchers.IO) {
            try {
                var data = ""
                var iStream: InputStream? = null
                var urlConnection: HttpURLConnection? = null
                try {
                    // Hazırladığımız url yi kullanarak web servise Http bağlantısı ile sağlıyoruz
                    val url = URL(url)
                    urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.connect()

                    // webservisden gelen json datayı okuyup, data değişkenine atadık
                    iStream = urlConnection.inputStream
                    val br = BufferedReader(InputStreamReader(iStream))
                    val sb = StringBuffer()
                    var line: String? = ""
                    while (br.readLine().also { line = it } != null) {
                        sb.append(line)
                    }
                    //json veriyi data değişkenine atadıkve metodda return yaptık
                    data = sb.toString()
                    br.close()
                } catch (e: Exception) {
                    //Log.d("Exception on download", e.toString());
                } finally {
                    iStream?.close()
                    urlConnection?.disconnect()
                }
                onPostExecute(data,mMap)
            } catch (e: Exception) {
                // Log.d("Background Task", e.toString())
            }
        }
    }

    private suspend fun onPostExecute(result: String,mMap: GoogleMap) {
        // Google Directions bilgisini, JSON formatını parse ederek almayı sağlayan sınıf
        val parserTask = ParserTask()
        parserTask.execute(result,mMap)
    }
}