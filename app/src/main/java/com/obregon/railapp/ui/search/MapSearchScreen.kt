package com.obregon.railapp.ui.search

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.obregon.railapp.R
import com.obregon.railapp.data.Station
import com.obregon.railapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_screen_layout.*


@AndroidEntryPoint
class MapSearchScreen:Fragment(R.layout.map_screen_fragment),OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: SearchScreenViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.activity?.let { activity ->
            viewModel.stationNames.observe(
                activity,
                { setupMap(activity) }
            )
        }
    }

    fun setupMap(activity: Activity){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap?.apply {

            uiSettings.isZoomControlsEnabled=true

            viewModel.stations.value?.let { it ->
                val stations:List<Station> =it
                lateinit var latLng1:LatLng
                for(station in stations){
                    val latLng=LatLng(
                        station.stationLatitude.toDouble(),
                        station.stationLongitude.toDouble()
                    )
                    if(station.stationDesc.contains("Heuston")){
                            latLng1=latLng
                    }
                    addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(station.stationDesc)
                    )

                    setOnMarkerClickListener {
                        navigate(it.title)
                    }
                }
                // [START_EXCLUDE silent]
                moveCamera(CameraUpdateFactory.newLatLng(latLng1))
                // [END_EXCLUDE]
            }
        }
    }

    private fun navigate(stationName: String):Boolean{
        (this.activity as MainActivity).navigateToHome(stationName)
        return true
    }

  /* private fun getDeviceLocation(googleMap: GoogleMap?) {
        try {
            if (mLocationPermissionGranted) {
                val locationResult: Task<Location> = fusedLocationClient.getLastLocation()
                locationResult.addOnCompleteListener(object : OnCompleteListener<Location?> {
                    override fun onComplete(task: Task<Location?>) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                                val location: Location? = task.getResult()
                               location?.let{
                                   val currentLatLng = LatLng(
                                    location.getLatitude(),
                                    location.getLongitude()
                                )
                                val update = CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng,
                                    DEFAULT_ZOOM
                                )
                                googleMap?.moveCamera(update)
                            }
                        }
                    }
                })
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message)
        }
    }


    fun checkPermissions(){
        AskPermission.Builder(this)
            .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .setCallback(/* PermissionCallback */)
            .setErrorCallback(/* ErrorCallback */)
            .request()
    }*/

}