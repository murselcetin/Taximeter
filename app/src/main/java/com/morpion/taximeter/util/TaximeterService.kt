package com.morpion.taximeter.util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import com.morpion.taximeter.R
import com.morpion.taximeter.util.Constants.ACTION_PAUSE_SERVICE
import com.morpion.taximeter.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.morpion.taximeter.util.Constants.ACTION_STOP_SERVICE
import com.morpion.taximeter.util.Constants.FASTEST_LOCATION_INTERVAL
import com.morpion.taximeter.util.Constants.LOCATION_UPDATE_INTERVAL
import com.morpion.taximeter.util.Constants.NOTIFICATION_CHANNEL_ID
import com.morpion.taximeter.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.morpion.taximeter.util.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

@AndroidEntryPoint
class TaximeterService : LifecycleService() {

    // Start stop kontrol
    var statusControl = true

    private var timerControl = false

    // setTimer() yakalamak için hizmet başlama zamanı
    private var timeStarted = 0L

    // setTimer() durdurma zamanı yakalama
    private var lapTime = 0L

    // toplam süre
    private var timeRun = 0L

    // Servis kontrolü
    private var serviceControl = false

    @Inject   // mevcut konumu almak için
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject // base bildirim
    lateinit var baseNotificationBuilder: NotificationCompat.Builder
    
    lateinit var curNotificationBuilder: NotificationCompat.Builder


    companion object {
        val taximeterControl = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>() // Rota listesi
        val timeRunInSeconds = MutableLiveData<Long>() // Toplam süre
        val paid = MutableLiveData<String>()
    }

    override fun onCreate() {
        super.onCreate()

        postInitialValues()
        curNotificationBuilder = baseNotificationBuilder
        taximeterControl.observe(this, Observer {
            // Taksimetre başladığında konum güncelleme
            updateLocationTaximeter(it)

            // Taksimetre başladığında bildirimi güncelleme
            updateNotificationTaximeterState(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (statusControl) {
                        startForegroundService()
                        statusControl = false
                    } else {
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService() {
        serviceControl = true
        statusControl = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    private fun pauseService() {
        taximeterControl.postValue(false)
        timerControl = false
    }

    // Bildirime data oluşturma
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, IMPORTANCE_LOW)
        notificationManager.createNotificationChannel(channel)
    }

    private fun startForegroundService() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }
        taximeterControl.postValue(true)
        startTimer()
        startForeground(NOTIFICATION_ID, baseNotificationBuilder.build())
        timeRunInSeconds.observe(this, Observer {
            if(!serviceControl) {
                val notification = curNotificationBuilder.setContentText(TaximeterUtility.getFormattedStopwatchTime(it))
                notificationManager.notify(NOTIFICATION_ID,notification.build())
            }
        })

    }

    private fun updateNotificationTaximeterState(taximeterControl: Boolean) {
        val notificationActionText = if(taximeterControl) "Duraklat" else "Devam Et"
        val pendingIntent = if(taximeterControl) {
            val pauseIntent = Intent(this,TaximeterService::class.java).apply {
                action = ACTION_PAUSE_SERVICE
            }
            PendingIntent.getService(this,1,pauseIntent,
                FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this,TaximeterService::class.java).apply {
                action = ACTION_START_OR_RESUME_SERVICE
            }
            PendingIntent.getService(this,2,resumeIntent, FLAG_IMMUTABLE or FLAG_UPDATE_CURRENT)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Bildirim veri temizleme
        curNotificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(curNotificationBuilder,ArrayList<NotificationCompat.Action>())
        }

        if(!serviceControl) {
            curNotificationBuilder = baseNotificationBuilder.addAction(R.drawable.ic_taxi,notificationActionText,pendingIntent)
            notificationManager.notify(NOTIFICATION_ID,curNotificationBuilder.build())
        }
    }

    private fun postInitialValues() {
        taximeterControl.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeRunInSeconds.postValue(0L)
        paid.postValue("0.0")
    }

    // Duraklama olduğunda boş çizgi ekleme
    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    // Konumları ekleme
    private fun addPathPoint(location: Location?) {
        location?.let {
            // Güncel konum alma
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (taximeterControl.value!!) {
                result?.locations?.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                    }
                }
            }
        }

    }

    @SuppressLint("MissingPermission") // Easypermissions kullandık o yüzden suppress ile izinleri geçtik
    private fun updateLocationTaximeter(taximeterControl: Boolean) {
        if (taximeterControl) {
            if (TaximeterUtility.hasLocationPermissions(this)) {
                val request = LocationRequest().apply {
                    interval = LOCATION_UPDATE_INTERVAL
                    fastestInterval = FASTEST_LOCATION_INTERVAL
                    priority = PRIORITY_HIGH_ACCURACY
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    // Kronometre
    private fun startTimer() {
        addEmptyPolyline()
        taximeterControl.postValue(true)
        timeStarted = System.currentTimeMillis()
        timerControl = true
        CoroutineScope(Dispatchers.Main).launch {
            while (taximeterControl.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunInSeconds.postValue((timeRun + lapTime)/1000)
                delay(1000)
            }
            timeRun += lapTime
        }
    }
}