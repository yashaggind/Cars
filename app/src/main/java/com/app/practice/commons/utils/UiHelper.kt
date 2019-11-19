package com.app.practice.commons.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import com.google.android.material.snackbar.Snackbar

class UiHelper(private val context : Context) {

    fun getConnectivityStatus() : Boolean
    {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun showSnackBar(view: View, content: String) = Snackbar.make(view, content, Snackbar.LENGTH_LONG).show()
}