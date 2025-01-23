package com.ssafy.witch.ui.appointment

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ssafy.witch.R
import com.ssafy.witch.base.BaseFragment
import com.ssafy.witch.databinding.DialogArrivalSuccess1Binding
import com.ssafy.witch.databinding.DialogArrivalSuccess2Binding
import com.ssafy.witch.databinding.FragmentMapBinding

class MapFragment : BaseFragment<FragmentMapBinding>(FragmentMapBinding::bind, R.layout.fragment_map),
    OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fg_fg) as SupportMapFragment
        mapFragment.getMapAsync(this)

//        showDialog(1)
    }

//    private fun showDialog(x: Int) {
//        val dialog= Dialog(requireContext())
//        var dialogBinding:
//        if (x == 1) {
//            dialogBinding= DialogArrivalSuccess1Binding.inflate(layoutInflater)
//        } else if (x == 2) {
//            dialogBinding= DialogArrivalSuccess2Binding.inflate(layoutInflater)
//        }
//        do
//    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            startLocationUpdates()
        }
    }

    lateinit var destLocation: Location
    private fun startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            val location = LatLng(36.108995, 128.421774)
            destLocation= Location("")
            destLocation.latitude= 36.108995
            destLocation.longitude= 128.421774
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

            val userMarkerBitmap = BitmapFactory.decodeResource(resources, R.drawable.dest)
            val scaledUserMarker = Bitmap.createScaledBitmap(userMarkerBitmap, 130, 130, false)
            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("도착지")
                    .icon(BitmapDescriptorFactory.fromBitmap(scaledUserMarker))
            )
        } else {
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}