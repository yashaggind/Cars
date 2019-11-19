package com.app.practice.ui.fragment

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.practice.R
import com.app.practice.commons.extensions.nonNull
import com.app.practice.commons.helper.GoogleMapHelper
import com.app.practice.commons.helper.GpsHelper
import com.app.practice.commons.helper.PermissionHelper
import com.app.practice.commons.helper.PermissionHelper.Companion.ACCESS_FINE_LOCATION
import com.app.practice.commons.helper.PermissionHelper.Companion.PERMISSIONS_REQUEST_LOCATION
import com.app.practice.commons.helper.UiHelper
import com.app.practice.ui.activity.CarActivity
import com.app.practice.vm.CarViewModel
import com.app.practice.vm.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CarMapFragment : Fragment(), OnMapReadyCallback, PermissionHelper.OnPermissionRequested
    ,GoogleMap.OnMarkerClickListener{

    // FOR DATA ---
    private val uiHelper : UiHelper by inject()
    private val locationVM : LocationViewModel by viewModel()
    private val carVM : CarViewModel by sharedViewModel()
    private val googleMapHelper : GoogleMapHelper by inject()
    private var mactivity : Activity? = null
    private var carActivity : CarActivity? = null
    private lateinit var mapView : MapView
    private var gpsHelper : GpsHelper? = null
    private var permissionHelper : PermissionHelper? = null
    private var googleMap : GoogleMap? = null
    private var currentLatLng : LatLng? = null
    private var isPermissionPermanentlyDenied = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        mactivity = activity
        carActivity = mactivity as CarActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.car_map_fragment, container, false)

        mapView = view.findViewById(R.id.mapView) as MapView
        mapView.onCreate(savedInstanceState)

        gpsHelper = carActivity?.let { GpsHelper(it,uiHelper) }

        permissionHelper = carActivity?.let { PermissionHelper(it,uiHelper) }

        if(!permissionHelper?.isPermissionGranted(ACCESS_FINE_LOCATION)!!)
            permissionHelper?.requestPermission(arrayOf(ACCESS_FINE_LOCATION),PERMISSIONS_REQUEST_LOCATION,this)
        else enableGps()

        mapView.onResume()

        return view
    }

    /*
     * Checking whether Location Permission is granted or not.
     * */

    private fun checkPermissionGranted() {
        if(carActivity?.let { ActivityCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED)
            permissionHelper?.openSettingsDialog()
        else enableGps()
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<out String>, grantResults : IntArray) {
        permissionHelper?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * This function is to get the result form [PermissionHelper] class
     *
     * @param isPermissionGranted the [Boolean]
     */

    override fun onPermissionResponse(isPermissionGranted : Boolean) {

        if(!isPermissionGranted) isPermissionPermanentlyDenied = true
        else enableGps()
    }

    private fun enableGps() {
        isPermissionPermanentlyDenied = false

        if (!uiHelper.isLocationProviderEnabled()) subscribeLocationObserver()
        else gpsHelper?.openGpsSettingDialog()
    }

    // Start Observing the User Current Location and set the marker to it.
    private fun subscribeLocationObserver()
    {
        // OBSERVABLES ---
        locationVM.currentLocation.nonNull().observe(this, Observer {

            currentLatLng = googleMapHelper.getLatLng(it.latitude,it.longitude)

            currentLatLng?.let{ data -> mapSetUp(data) }

            locationVM.stopLocationUpdates()
        })

        locationVM.requestLocationUpdates()
    }

    // Add a marker to the current Location, and move the camera.
    private fun mapSetUp(latLing : LatLng?) {
        if(googleMap != null) {
            googleMap?.addMarker(latLing?.let { googleMapHelper.addCurrentLocationMarker(it) })
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLing, 14.0F))

            // OBSERVABLES ---
            carVM.carData.observe(this,Observer{

                it?.let {
                    for (i in it.indices)
                        googleMap?.addMarker(it[i].coordinates?.let { data ->
                            googleMapHelper.addMarker(data)})?.tag = it[i]
                }
            })
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        googleMap?.let { googleMapHelper.defaultMapSettings(it) }
        googleMap?.setOnMarkerClickListener(this)
    }

    /** Called when the user clicks a marker. */
    override fun onMarkerClick(marker : Marker?) : Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()

        if(isPermissionPermanentlyDenied) checkPermissionGranted()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}