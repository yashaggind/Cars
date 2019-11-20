package com.app.practice.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.app.practice.R
import com.app.practice.commons.helper.UiHelper
import com.app.practice.datasource.api.NetworkState
import com.app.practice.ui.fragment.CarListFragment
import com.app.practice.ui.fragment.CarMapFragment
import com.app.practice.ui.viewpager.ViewPagerAdapter
import com.app.practice.vm.CarViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_car.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class CarActivity : AppCompatActivity(){

    // FOR DATA ---
    private val carVM: CarViewModel by viewModel()
    private val uiHelper: UiHelper by inject()
    private lateinit var adapter : ViewPagerAdapter
    private val TAG : String = "CarActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car)

        checkPlayServicesAvailable()

        /*
        * Check Internet Connection
        * */

        if (uiHelper.getConnectivityStatus()) configureObservables()
        else uiHelper.showSnackBar(car_rootView,
            resources.getString(R.string.error_message_network))
    }

    /*
     * Checking out is Google Play Services app is installed or not.
     * */

    private fun checkPlayServicesAvailable() {
        if(!uiHelper.isPlayServicesAvailable()) {
            uiHelper.toast(resources.getString(R.string.play_service_not_installed))
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode : Int, permissions : Array<out String>, grantResults : IntArray) {
        permissionsResult(requestCode,permissions,grantResults)
    }

    private fun permissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val pos = viewPager.currentItem
        val fragment = adapter.getItem(pos)
        if(pos == 0) fragment.onRequestPermissionsResult(requestCode,permissions,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activityResult(requestCode, resultCode, data)
    }

    private fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val pos = viewPager.currentItem
        val fragment = adapter.getItem(pos)
        if(pos == 0) fragment.onActivityResult(requestCode,resultCode,data)
    }

    private fun setupViewPager() {

        adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(CarMapFragment(), resources.getString(R.string.map))
        adapter.addFragment(CarListFragment(), resources.getString(R.string.list))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                //setCurrentItem is used to visible Item of View Pager of Selected Tab Position.
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        tabLayout.setupWithViewPager(viewPager)
    }

    private fun configureObservables() {

        carVM.carData.observe(this, Observer {

            it?.let {
                Log.e(TAG,"CarData address: ${it[0].address}")
                Log.e(TAG,"CarData address Latitude: ${it[0].coordinates?.get(0)}")
                Log.e(TAG,"CarData address Longitude: ${it[0].coordinates?.get(1)}")
                Log.e(TAG,"CarData Name: ${it[0].name}")

                if(it.isNotEmpty()) setupViewPager()
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
