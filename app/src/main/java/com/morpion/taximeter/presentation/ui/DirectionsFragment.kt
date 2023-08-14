package com.morpion.taximeter.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.morpion.taximeter.R
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.databinding.FragmentDirectionsBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.util.Constants
import com.morpion.taximeter.util.DownloadTask
import com.morpion.taximeter.util.LocalSessions
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


class DirectionsFragment : BaseFragment<FragmentDirectionsBinding>(FragmentDirectionsBinding::inflate) {

    private var lastStartLocation: MarkerOptions? = null
    private var lastEndLocation: MarkerOptions? = null

    @Inject
    lateinit var sessions: LocalSessions

    var mMap: GoogleMap? = null

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiKey = "AIzaSyDV2pGO9KXLicf5z6s7AOa_dpb8gKRQrnw"

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }

        binding.clInfo.visibility = View.GONE

        binding.mapView.onCreate(savedInstanceState)

        binding.mapView.getMapAsync {
            mMap = it
        }

        binding.btnNavigate.setSafeOnClickListener {
            navigateGoogleMapDirections()
        }

        val startLocation = childFragmentManager.findFragmentById(R.id.frg_start_location) as AutocompleteSupportFragment?
        val endLocation = childFragmentManager.findFragmentById(R.id.frg_end_location) as AutocompleteSupportFragment?
        startLocation?.setText("Mevcut Konum")

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()

        startLocation!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
            )
        )

        startLocation.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val location = place.latLng?.let { LatLng(it.latitude, it.longitude) }
                binding.mapView.getMapAsync {
                    it.clear()
                    lastStartLocation = MarkerOptions().position(location).title(place.name)
                    it.addMarker(lastStartLocation)
                    it?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            location,
                            Constants.MAP_ZOOM
                        )
                    )
                    if(lastEndLocation != null) {
                        it.addMarker(lastEndLocation)
                        lastStartLocation?.position?.let { it1 -> lastEndLocation?.position?.let { it2 ->
                            lifecycleScope.launch {
                                drawRoute(it1, it2)
                            }
                        } }
                    }
                }
            }

            override fun onError(status: Status) {
            }
        })

        endLocation!!.setPlaceFields(
            listOf(
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG,
            )
        )

        endLocation.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                binding.clInfo.visibility = View.VISIBLE
                val location = place.latLng?.let { LatLng(it.latitude, it.longitude) }
                binding.mapView.getMapAsync {
                    it.clear()
                    lastEndLocation = MarkerOptions().position(location).title(place.name)
                    it.addMarker(lastEndLocation)
                    it?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            location,
                            Constants.MAP_ZOOM
                        )
                    )
                    if(lastStartLocation != null) {
                        it.addMarker(lastStartLocation)
                        lastStartLocation?.position?.let { it1 -> lastEndLocation?.position?.let { it2 ->
                            lifecycleScope.launch {
                                drawRoute(it1, it2)
                            }
                        } }
                    }
                }
            }

            override fun onError(status: Status) {
            }
        })
    }

    private fun navigateGoogleMapDirections() {
        val gmmIntentUri = Uri.parse("google.navigation:q=${lastEndLocation?.position?.latitude},${lastEndLocation?.position?.longitude}&avoid=tf")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
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

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        binding.mapView.getMapAsync {
                            lastStartLocation = MarkerOptions().position(LatLng(list[0].latitude,list[0].longitude)).title("Mevcut Konum")
                            it.addMarker(lastStartLocation)
                            it?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(list[0].latitude,list[0].longitude),
                                    Constants.MAP_ZOOM
                                )
                            )
                        }
                    }
                }
            }
        } else {
            requestPermissions()
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateHomeFragment()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

    private fun navigateHomeFragment() {
        val action = DirectionsFragmentDirections.actionDirectionsFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private suspend fun drawRoute(startLocation: LatLng, endLocation: LatLng) {
        val url = getDirectionsUrl(startLocation, endLocation)
        val downloadTask = DownloadTask()
        mMap?.let { downloadTask.execute(url, it) }
    }

    private fun getDirectionsUrl(startLocation: LatLng, endLocation: LatLng): String {
        val startText = "origin=" + startLocation.latitude + "," + startLocation.longitude
        val endText = "destination=" + endLocation.latitude + "," + endLocation.longitude

        // API Key parametre olarak hazırlama
        val key = "key=" + "AIzaSyDV2pGO9KXLicf5z6s7AOa_dpb8gKRQrnw"
        val parameters = "$startText&$endText&$key"
        val output = "json"

        // Oluşturduğumuz parametreleri kullanarak url yi oluşturuyoruz
        Log.e("TAG", "https://maps.googleapis.com/maps/api/directions/$output?$parameters", )
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }
}

