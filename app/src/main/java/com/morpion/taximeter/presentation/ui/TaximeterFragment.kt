package com.morpion.taximeter.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.morpion.taximeter.R
import com.morpion.taximeter.data.local.entity.TaximeterHistoryLocalData
import com.morpion.taximeter.databinding.FragmentTaximeterBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.presentation.ui.viewmodel.TaximeterViewModel
import com.morpion.taximeter.util.Constants.ACTION_PAUSE_SERVICE
import com.morpion.taximeter.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.morpion.taximeter.util.Constants.ACTION_STOP_SERVICE
import com.morpion.taximeter.util.Constants.MAP_ZOOM
import com.morpion.taximeter.util.Constants.POLYLINE_COLOR
import com.morpion.taximeter.util.Constants.POLYLINE_WIDTH
import com.morpion.taximeter.util.LocalSessions
import com.morpion.taximeter.util.Polyline
import com.morpion.taximeter.util.TaximeterService
import com.morpion.taximeter.util.TaximeterUtility
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Math.round
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TaximeterFragment : BaseFragment<FragmentTaximeterBinding>(FragmentTaximeterBinding::inflate) {

    private val viewModel: TaximeterViewModel by viewModels()

    @Inject
    lateinit var sessions: LocalSessions

    private var map: GoogleMap? = null
    // taksimetre çalışma kontrolü
    private var taximeterControl = false
    // aldığımız yol rotası
    private var pathPoints = mutableListOf<Polyline>()
    // taksimetre süresi
    private var curTimeInSeconds = 0L
    // katedilen mesafe
    private var distance = 0f
    // Hız
    private var avgSpeed = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
            map = it
            addAllPolylines()
        }

        binding.btnStartService.setOnClickListener {
            toggleTaximeter()
        }

        binding.btnStopService.setOnClickListener {
            endTaximeterAndSaveToDb()
        }
    }

    private fun endTaximeterAndSaveToDb() {
        binding.btnStopService.visibility = View.GONE
        binding.btnStartService.visibility = View.VISIBLE
        map?.snapshot { bmp ->
            var distance = 0
            for(polyline in pathPoints) {
                distance += (TaximeterUtility.calculatePolylineLength(polyline).toInt())/1000
            }
            val curDate = Calendar.getInstance().timeInMillis
            stopTaximeter()
            viewModel.saveTaximeter(TaximeterHistoryLocalData(
                id = 0,
                paid = binding.tvPaid.text.toString(),
                distance = binding.tvDistance.text.toString(),
                time = binding.tvTime.text.toString(),
                date = curDate,
                img = bmp
            ))
        }
        map?.clear()
    }

    // Son çizgiyi ekleme
    private fun addLatestPolyline() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size>1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size-2]
            val lastLatLng = pathPoints.last().last()

            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)

            // Çoklu çizgi ekleme
            map?.addPolyline(polylineOptions)
        }
    }

    // Normal çizgi ekleme
    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    // Harita görünümünü kullanıcıya hizalama
    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }

    private fun updateTaximeterStatus(status: Boolean) {
        this.taximeterControl = status
        if(!status) {
            binding.btnStartService.text = "Başlat"
        } else {
            binding.btnStartService.text = "Duraklat"
        }
    }

    // Servis duraklatma devam ettirme
    private fun toggleTaximeter() {
        binding.btnStopService.visibility = View.VISIBLE
        if(taximeterControl) {
            sendCommandToService(ACTION_PAUSE_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun subscribeToObservers() {
        TaximeterService.taximeterControl.observe(viewLifecycleOwner, Observer {
            updateTaximeterStatus(it)
        })
        TaximeterService.pathPoints.observe(viewLifecycleOwner, Observer{
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
            val distTaximeter = it
            distance = TaximeterUtility.calculateLengthofPolylines(distTaximeter)
            val distanceFormat = round((distance / 1000f) * 10) /10f
            val paid = (sessions.taximeterStartPrice?.toDouble()?:0.0) + (distanceFormat.toString().toDouble() * (sessions.taximeterKmPrice?.toDouble()?:0.0))
            binding.tvDistance.text = distanceFormat.toString()
            binding.tvPaid.text = paid.toString()
        })

        TaximeterService.timeRunInSeconds.observe(viewLifecycleOwner, Observer {
            curTimeInSeconds = it
            val formattedTime = TaximeterUtility.getFormattedStopwatchTime(curTimeInSeconds)
            binding.tvTime.text = formattedTime
            avgSpeed = round(((distance/curTimeInSeconds)*(3600/1000))*10)/10
            binding.tvSpeed.text = avgSpeed.toString()
            if(curTimeInSeconds>0L) {
                binding.btnStopService.visibility = View.VISIBLE
            }
        })

        TaximeterService.paid.observe(viewLifecycleOwner, Observer {
            binding.tvPaid.text = it
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (taximeterControl)
                    {
                        val dialog = MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Taksimetre")
                            .setMessage("Taksimetre'yi durdurmak istediğinize emin misiniz?")
                            .setIcon(R.drawable.ic_taxi)
                            .setPositiveButton("Evet") { _,_ -> stopTaximeter() }
                            .setNegativeButton("Hayır") { d,_ -> d.cancel() }
                            .create()

                        dialog.show()
                    }else {
                        navigateHomeFragment()
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun navigateHomeFragment(){
        val action = TaximeterFragmentDirections.actionTaximeterFragmentToHomeFragment()
        findNavController().navigate(action)
    }
    
    private fun stopTaximeter() {
        sendCommandToService(ACTION_STOP_SERVICE)
        map?.clear()
    }

    private fun sendCommandToService(action: String) {
        val intent = Intent(requireContext(), TaximeterService::class.java).apply {
            this.action = action
        }
        requireContext().startService(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
         binding.mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
         binding.mapView.onSaveInstanceState(outState)
    }
}