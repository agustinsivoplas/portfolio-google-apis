package com.uy.atomico.googleapisportfolio.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import com.uy.atomico.googleapisportfolio.R
import com.uy.atomico.googleapisportfolio.base.BaseActivity
import kotlinx.android.synthetic.main.activity_maps.*

/**
 * Created by agustin.sivoplas@gmail.com on 11/20/18.
 * Atomico Labs
 */

class MapsActivity : BaseActivity(), OnMapReadyCallback {
    companion object {
        const val TAG = "MapsActivity"

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MapsActivity::class.java))
        }
    }

    override fun getLayoutResId() = R.layout.activity_maps

    private val rxPermissions by lazy {
        RxPermissions(this)
    }

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var mMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.view?.visibility = View.GONE

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        requestLocationPermission()

        allowLocationButton.setOnClickListener {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe { granted ->
                    if (granted) {
                        emptyViewLayout.visibility = View.GONE
                        mapFragment.view?.visibility = View.VISIBLE
                        mapFragment.getMapAsync(this@MapsActivity)
                    } else {
                        mapFragment.view?.visibility = View.GONE
                        emptyViewLayout.visibility = View.VISIBLE
                    }
                }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        Log.i(TAG, "onMapReady")
        mMap = map

        try {
            mMap?.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, com.uy.atomico.googleapisportfolio.R.raw.style_json))
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }

        if (rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            zoomIntoMapCurrentLocation()
        } else {
            rxPermissions
                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .filter { true }
                    .subscribe {
                        zoomIntoMapCurrentLocation()
                    }
        }
    }

    @SuppressLint("MissingPermission")
    private fun zoomIntoMapCurrentLocation() {
        mMap?.isMyLocationEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = false
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f))
                    }
                }
    }
}