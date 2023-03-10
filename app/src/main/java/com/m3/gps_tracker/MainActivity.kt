package com.m3.gps_tracker

import android.Manifest
import android.annotation.SuppressLint
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.m3.islami2.base.BaseActivity

class MainActivity : BaseActivity() , OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var map: SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (isLocationPermissionGranted()){

            showUserLocation()
        }
        else{
            requestLocationPermissionFromUser()

        }
        map=supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)


    }



    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {

                showUserLocation()
            } else {
                Toast.makeText(this, "Application need permission to continue ", Toast.LENGTH_SHORT).show()

            }
        }


    private fun requestLocationPermissionFromUser() {

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                showDialoge(message = "app needs to know your location"
                    , posActionName = "ok",
                    posAction = { dialog, which ->
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

                        dialog.dismiss()

                    }, negActionName = "no", negAction = { dialog, which ->
                        dialog.dismiss()
                    }
                )
            }
            else{
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }




    private fun isLocationPermissionGranted(): Boolean {
            return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED






        }

    private fun showUserLocation() {
        satisfySettingsToStarttrackLocation()
        Toast.makeText(this, "showing location", Toast.LENGTH_SHORT).show()


    }
    val locationRequest = LocationRequest.create().apply {
        interval = 4000


        fastestInterval = 10000

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY //test5dem eh 34an tgebh
    }


    val REQUEST_CHECK_SETTINGS=200


    private fun satisfySettingsToStarttrackLocation() {

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest!!)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { locationSettingsResponse ->

            startUserLocationTracking()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){


                try {

                    exception.startResolutionForResult(this@MainActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }



    }
    val locationCallback=object :LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            result ?: return

            for (location in result.locations) {

                drawUserLocation(location)

                Log.e("new location", "" + location.longitude + " " + location.longitude)
            }
        }
    }
    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }


    @SuppressLint("MissingPermission")
    private fun startUserLocationTracking() {
        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,//^^
            Looper.getMainLooper())
    }

    var googleMap: GoogleMap?=null
    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap=googleMap
    }
    var userMarker: Marker?=null
    fun drawUserLocation(location: Location){
        val markOption= MarkerOptions().position(LatLng( location.latitude,location.longitude))
        if (userMarker==null){
            userMarker=googleMap?.addMarker(markOption)
        }
        else{userMarker?.position= LatLng( location.latitude,location.longitude)
        }
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude),19f))
    }

}

