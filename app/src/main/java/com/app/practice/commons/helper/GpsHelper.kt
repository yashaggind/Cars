package com.app.practice.commons.helper

import android.app.Activity
import android.content.IntentSender
import com.app.practice.commons.utils.Constants.Companion.GPS_REQUEST_LOCATION
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

class GpsHelper constructor(private val activity : Activity, private val uiHelper : UiHelper)
{
    fun openGpsSettingDialog()
    {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(uiHelper.getLocationRequest())

        val client: SettingsClient = LocationServices.getSettingsClient(activity)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException)
            {
                try
                {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(activity, GPS_REQUEST_LOCATION)
                }
                catch (sendEx: IntentSender.SendIntentException)
                {
                    sendEx.printStackTrace()
                }
            }
        }
    }
}