package com.app.practice.commons.helper

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.app.practice.R
import com.app.practice.commons.callBacks.GpsEnableListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar

class UiHelper(private val context : Context) {

    fun getConnectivityStatus() : Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showSnackBar(view: View, content: String) = Snackbar.make(view, content, Snackbar.LENGTH_LONG).show()

    fun toast(content: String) = Toast.makeText(context, content, Toast.LENGTH_LONG).show()

    fun showPositiveDialogWithListener(activity: Activity, title: String, content: String,
                                       listener: GpsEnableListener,
                                       positiveText: String, cancelable: Boolean)
    {
        val builder = AlertDialog.Builder(activity, R.style.MyAlertDialogStyle)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setCancelable(cancelable)
        builder.setPositiveButton(positiveText){ dialog, _ ->
            listener.onPositive()
            dialog.dismiss()
        }

        val alert = builder.create()

        if(!alert.isShowing)
            alert.show()
    }

    fun getLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 3000
        return locationRequest
    }

    fun isLocationProviderEnabled() : Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    fun isPlayServicesAvailable() : Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return ConnectionResult.SUCCESS == status
    }
}