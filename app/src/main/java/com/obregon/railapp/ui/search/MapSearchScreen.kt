package com.obregon.railapp.ui.search

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.obregon.railapp.R
import com.obregon.railapp.data.Station
import com.obregon.railapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val FINE_LOCATION_PERMISSION_REQUEST=1

@AndroidEntryPoint
class MapSearchScreen:Fragment(R.layout.map_screen_fragment),OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val viewModel: SearchScreenViewModel by viewModels()
    private var currentLatLng:LatLng?=null
    private var isPermissionGranted:Boolean=false
    private val HOME_MARKER_TITLE:String="Your current location"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkPermissions()
        this.activity?.let { activity ->
            viewModel.stationNames.observe(
                activity,
                { setupMap() }
            )
        }
    }

    fun setupMap(){
        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        googleMap?.apply {

            uiSettings.isZoomControlsEnabled=true
            lateinit var fallBackLatLng:LatLng
            viewModel.stations.value?.let { it ->
                val stations:List<Station> =it

                for(station in stations){
                    val latLng=LatLng(
                        station.stationLatitude.toDouble(),
                        station.stationLongitude.toDouble()
                    )
                    if(station.stationDesc.contains("Connolly")){
                        fallBackLatLng= LatLng(station.stationLatitude.toDouble(),
                            station.stationLongitude.toDouble())
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

                zoomToDeviceLocation(googleMap,fallBackLatLng)

            }
        }
    }

    private fun navigate(stationName: String):Boolean{
        if(!stationName.contains(HOME_MARKER_TITLE)){ //hack
            (this.activity as MainActivity).navigateToHome(stationName)
        }
        return true
    }

    private fun addMarker(googleMap: GoogleMap?,latLng: LatLng,title:String,bitmapDescriptor: BitmapDescriptor){
        googleMap?.apply {
            addMarker(
                MarkerOptions()
                    .icon(bitmapDescriptor)
                    .position(latLng)
                    .title(title)
            )
        }
    }
    private fun zoomToDeviceLocation(googleMap: GoogleMap?,fallBackLatLng:LatLng) {
        try {
            if (isPermissionGranted) {
                val locationResult: Task<Location> = fusedLocationClient.lastLocation
                locationResult.addOnSuccessListener { location ->
                        run {
                            if(location!=null){
                                currentLatLng = LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            }else{ //fallback to Connlly for demo purposes if latlng is null
                                currentLatLng=fallBackLatLng
                            }
                            currentLatLng?.let {
                             addMarker(
                                 googleMap,
                                 currentLatLng as LatLng,
                                 HOME_MARKER_TITLE,
                                 BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                             )
                             val update = CameraUpdateFactory.newLatLngZoom(
                                 currentLatLng,
                                 13F
                             )
                             googleMap?.moveCamera(update)
                            }
                    }
                }
            }
        } catch (e: SecurityException) {
            Timber.e(e)
        }catch (e:NullPointerException) {
            Timber.e(e)
        }

    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        initClient()
        startLocationUpdates()
        getLastLocation()
    }

    override fun onPause() {
        stopLocationUpdates()
        super.onPause()
    }

    private fun initClient(){
         activity?.let {
             fusedLocationClient =LocationServices.getFusedLocationProviderClient(it)
         }
    }
    private fun getLastLocation(){
        try {
            if (isPermissionGranted) {
                val locationResult: Task<Location> = fusedLocationClient.lastLocation
                locationResult.addOnSuccessListener { location ->
                    run {
                        if(location!=null){
                            currentLatLng = LatLng(
                                location.latitude,
                                location.longitude
                            )
                        }
                    }
                }
            }
        }catch (e: SecurityException) {
            Timber.e(e)
        }
    }

    private fun checkPermissions(){
        if (this.context?.let {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    FINE_LOCATION_PERMISSION_REQUEST
                )
            }
        } else {
            isPermissionGranted = true
        }
    }

    private val locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback?=null


    private fun stopLocationUpdates() {
      locationCallback?.let {
        fusedLocationClient.removeLocationUpdates(locationCallback)
      }
    }

    private fun initLocationCallback(){
        locationCallback = object:LocationCallback() {
            @Override
            override fun onLocationResult(locationResult: LocationResult) {
                for (location:Location in locationResult.locations) {
                    currentLatLng= LatLng(location.latitude,location.longitude)
                }
            }
        }
    }

    private fun startLocationUpdates() {
        try{
        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions()
            return
        }
            initLocationCallback()
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
        }catch (e:NullPointerException){
            Timber.e(e)
        }
    }
}