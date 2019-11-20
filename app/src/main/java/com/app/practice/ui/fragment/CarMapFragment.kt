package com.app.practice.ui.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.app.practice.R
import com.app.practice.commons.callBacks.GpsEnableListener
import com.app.practice.commons.extensions.nonNull
import com.app.practice.commons.helper.GoogleMapHelper
import com.app.practice.commons.helper.GpsHelper
import com.app.practice.commons.helper.PermissionHelper
import com.app.practice.commons.helper.PermissionHelper.Companion.ACCESS_FINE_LOCATION
import com.app.practice.commons.helper.PermissionHelper.Companion.PERMISSIONS_REQUEST_LOCATION
import com.app.practice.commons.helper.UiHelper
import com.app.practice.commons.utils.Constants.Companion.GPS_REQUEST_LOCATION
import com.app.practice.ui.activity.CarActivity
import com.app.practice.vm.CarViewModel
import com.app.practice.vm.LocationViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CarMapFragment : Fragment(), OnMapReadyCallback,PermissionHelper.OnPermissionRequested
    ,GoogleMap.OnMarkerClickListener{

    // FOR DATA ---
    private val uiHelper : UiHelper by inject()
    private val locationVM : LocationViewModel by viewModel()
    private val carVM : CarViewModel by sharedViewModel()
    private val googleMapHelper : GoogleMapHelper by inject()
    private var mactivity : Activity? = null
    private var carActivity : CarActivity? = null
    private var gpsHelper : GpsHelper? = null
    private var permissionHelper : PermissionHelper? = null
    private var googleMap : GoogleMap? = null
    private var currentLatLng : LatLng? = null
    private var isPermissionPermanentlyDenied = false
    private lateinit var markersArrayList : ArrayList<Marker>
    private var marker : Marker? = null
    private val TAG : String = "CarActivity"

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

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        gpsHelper = carActivity?.let { GpsHelper(it,uiHelper) }

        permissionHelper = mactivity?.let { PermissionHelper(it,uiHelper) }

        if(!permissionHelper?.isPermissionGranted(ACCESS_FINE_LOCATION)!!)
            permissionHelper?.requestPermission(arrayOf(ACCESS_FINE_LOCATION),PERMISSIONS_REQUEST_LOCATION,this)
        else enableGps()

        return view
    }

    override fun onResume() {
        super.onResume()
        if(isPermissionPermanentlyDenied) checkPermissionGranted()
    }

    /*
     * Checking whether Location Permission is granted or not.
     * */

    private fun checkPermissionGranted() {
        if(carActivity?.let { ContextCompat.checkSelfPermission(it, ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED)
            permissionHelper?.openSettingsDialog()
        else enableGps()
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<out String>, grantResults : IntArray) {
        permissionHelper?.onRequestPermissionsResult(requestCode,permissions, grantResults)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_REQUEST_LOCATION ->
                when (resultCode) {
                    AppCompatActivity.RESULT_OK -> subscribeLocationObserver()

                    AppCompatActivity.RESULT_CANCELED -> {
                        mactivity?.let {
                            uiHelper.showPositiveDialogWithListener(it,
                                resources.getString(R.string.need_location),
                                resources.getString(R.string.location_content),
                                object : GpsEnableListener {
                                    override fun onPositive() {
                                        enableGps()
                                    }
                                }, resources.getString(R.string.turn_on), false)
                        }
                    }
                }
        }
    }

    // Start Observing the User Current Location and set the marker to it.
    private fun subscribeLocationObserver()
    {
        Log.e(TAG,"CarMapFragment subscribeLocationObserver")

        // OBSERVABLES ---
        locationVM.currentLocation.nonNull().observe(this, Observer {

            currentLatLng = googleMapHelper.getLatLng(it.latitude,it.longitude)

            Log.e(TAG,"CarMapFragment latitude: ${it.latitude}")
            Log.e(TAG,"CarMapFragment longitude: ${it.longitude}")

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
                    markersArrayList = ArrayList()
                    for (i in it.indices)
                    {
                        googleMap?.addMarker(it[i].let { data ->
                            googleMapHelper.addMarker(data)})?.tag = it[i].name

                        marker = googleMap?.addMarker(it[i].let { data ->
                            googleMapHelper.addMarker(data)})

                        marker?.let { data -> markersArrayList.add(data) }
                    }
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

        Log.e(TAG,"onMarkerClick marker Tag: ${marker?.tag}")

        for (item in markersArrayList.indices) {
            Log.e(TAG,"onMarkerClick ArrayList Tag id: ${markersArrayList[item].title}")

            if (markersArrayList[item].title != marker?.tag) {
                markersArrayList[item].setVisible(false)
                Log.e(TAG,"onMarkerClick marker if Tag: ${markersArrayList[item].title}")
            }
            else Log.e(TAG,"onMarkerClick marker else Tag: ${markersArrayList[item].title}")
        }

        return false
    }
}