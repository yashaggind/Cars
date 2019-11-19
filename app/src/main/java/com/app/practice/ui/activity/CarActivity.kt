package com.app.practice.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.app.practice.R
import com.app.practice.commons.utils.UiHelper
import com.app.practice.datasource.api.NetworkState
import com.app.practice.vm.CarViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CarActivity : AppCompatActivity() {

    // FOR DATA ---
    private val carVM: CarViewModel by viewModel()
    private val uiHelper: UiHelper by inject()
    private val TAG : String = "CarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * Check Internet Connection
        * */

        if (uiHelper.getConnectivityStatus()) configureObservables()
        else uiHelper.showSnackBar(car_rootView,
            resources.getString(R.string.error_message_network))
    }

    private fun configureObservables() {

        carVM.carData.observe(this, Observer {

            it?.let {
                Log.e(TAG,"CarData address: ${it[0].address}")
                Log.e(TAG,"CarData address Latitude: ${it[0].coordinates?.get(0)}")
                Log.e(TAG,"CarData address Longitude: ${it[0].coordinates?.get(1)}")
                Log.e(TAG,"CarData Name: ${it[0].name}")
            }
        })

        /*
         * Progress Updater
         * */
        carVM.networkState.observe(this, Observer {
            if (it != null) {
                when (it) {
                    is NetworkState.Loading -> showProgressBar(true)
                    is NetworkState.Success -> showProgressBar(false)
                    is NetworkState.Error -> {
                        uiHelper.showSnackBar(car_rootView, it.code.toString())
                        showProgressBar(false)
                    }
                }
            }
        })
    }

    // UPDATE UI ----
    private fun showProgressBar(display: Boolean) {
        if (!display) progress_bar.visibility = View.GONE
        else progress_bar.visibility = View.VISIBLE
    }
}
