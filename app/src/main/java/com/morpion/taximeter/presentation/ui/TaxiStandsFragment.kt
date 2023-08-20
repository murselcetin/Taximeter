package com.morpion.taximeter.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.morpion.taximeter.common.extensions.setSafeOnClickListener
import com.morpion.taximeter.common.extensions.toPhoneFormat
import com.morpion.taximeter.databinding.FragmentTaxiStandsBinding
import com.morpion.taximeter.presentation.base.BaseFragment
import com.morpion.taximeter.shared.TaxiStandsManager
import com.morpion.taximeter.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class TaxiStandsFragment :
    BaseFragment<FragmentTaxiStandsBinding>(FragmentTaxiStandsBinding::inflate) {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    @Inject
    lateinit var taxiStandsManager: TaxiStandsManager

    private var lastSelectedTaxiStandsPhone = "0"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.map.onCreate(savedInstanceState)

        addTaxiStandsMarkers()

        binding.btnCall.setSafeOnClickListener {
            callTaxiStands()
        }

        binding.map.getMapAsync {
            it.setOnMarkerClickListener { marker ->
                if (marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                    binding.btnCall.visibility = View.GONE
                } else {
                    marker.showInfoWindow()
                    binding.btnCall.visibility = View.VISIBLE
                    lastSelectedTaxiStandsPhone = marker.title?.takeLast(16).toString()
                }
                true
            }
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        getLocation()
    }

    private fun callTaxiStands() {
        val intentUri = Uri.parse("tel:"+"$lastSelectedTaxiStandsPhone")
        val dialIntent = Intent(Intent.ACTION_DIAL, intentUri)
        startActivity(dialIntent)
    }

    private fun addTaxiStandsMarkers() {
        taxiStandsManager.getTaxiStands().forEach { itTaxiStands ->
            val location = LatLng(itTaxiStands.latitude?.toDouble() ?: 0.0, itTaxiStands.longitude?.toDouble() ?: 0.0)
            binding.map.getMapAsync {
                val phone = itTaxiStands.durakTel?:""
                it.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title("${itTaxiStands.durakAd}, ${phone.toPhoneFormat()} ")
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.map.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map.onDestroy()
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
        val action = TaxiStandsFragmentDirections.actionTaxiStandsFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        binding.map.getMapAsync {
                            it?.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(list[0].latitude, list[0].longitude),
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
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
}