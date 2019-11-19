package com.app.practice.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.practice.R
import com.app.practice.commons.callBacks.DiffUtilCallBack
import com.app.practice.databinding.AdapterCarListBinding
import com.app.practice.domain.entities.PlaceMarks
import kotlin.properties.Delegates

class CarListAdapter : RecyclerView.Adapter<CarListAdapter.MyViewHolder>(),
    DiffUtilCallBack {

    var placeMarks : List<PlaceMarks> by Delegates.observable(emptyList()) { _, oldItem, newItem ->
        autoNotify(oldItem, newItem) { old, new -> old.vehicleInsurance == new.vehicleInsurance }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: AdapterCarListBinding = DataBindingUtil
            .inflate(LayoutInflater.from(parent.context), R.layout.adapter_car_list, parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(placeMarks[position])
    }

    // Gets the number of Items in the list
    override fun getItemCount(): Int = placeMarks.size

    inner class MyViewHolder(private val binding: AdapterCarListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(placeMarks: PlaceMarks) {
            binding.placeMarks = placeMarks
        }
    }
}
