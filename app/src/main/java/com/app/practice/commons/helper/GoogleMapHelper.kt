package com.app.practice.commons.helper

import com.app.practice.R
import com.app.practice.domain.entities.PlaceMarks
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

class GoogleMapHelper {

    /**
     * @param placeMarks where to draw the [PlaceMarks]
     * @return the [MarkerOptions] with given properties added to it.
     */

    fun addMarker(placeMarks: PlaceMarks): MarkerOptions? =
        placeMarks.let {
            placeMarks.coordinates?.get(1)?.let { data ->
                LatLng(data, placeMarks.coordinates[0]).let {
                    MarkerOptions().position(it).anchor(0.5f, 0.5f)
                        .title(placeMarks.name).icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon))
                }
            }
        }

    /**
     * @param latLng where to draw the current location [Marker]
     * @return the [MarkerOptions] with given properties added to it.
     */

    fun addCurrentLocationMarker(latLng: LatLng): MarkerOptions = MarkerOptions().position(latLng)

    /**
     * @param latitude
     * @param longitude
     * @return the [LatLng]
     */

    fun getLatLng(latitude: Double, longitude: Double) = LatLng(latitude, longitude)

    /**
     * This function sets the default google map settings.
     *
     * @param googleMap to set default settings.
     */

    fun defaultMapSettings(googleMap: GoogleMap) {
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.isBuildingsEnabled = true
    }
}