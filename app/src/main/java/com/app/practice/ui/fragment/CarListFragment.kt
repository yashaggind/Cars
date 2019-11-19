package com.app.practice.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.practice.R
import com.app.practice.ui.adapter.CarListAdapter
import com.app.practice.vm.CarViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CarListFragment : Fragment() {

    private val carVM: CarViewModel by sharedViewModel()
    private lateinit var recylvCar : RecyclerView
    private var carListAdapter = CarListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.car_list_fragment, container, false)
        recylvCar = view.findViewById(R.id.car_recylv)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()
        configureObservables()
    }

    private fun configureObservables() {

        carVM.carData.observe(this, Observer {
            it?.let { carListAdapter.placeMarks = it }
        })
    }

    private fun initRecyclerView() {
        /*
         * Setup the adapter class for the RecyclerView
         * */

        recylvCar.let {
            recylvCar.layoutManager = LinearLayoutManager(activity)
            recylvCar.adapter = carListAdapter
        }
    }
}