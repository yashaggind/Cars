package com.app.practice.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.app.practice.R

@BindingAdapter(value = ["latitude"])
fun latitude(textView : TextView, coordinates: Array<Double>?) {

    coordinates?.let {
        val latitude  = textView.context.resources.getString(R.string.latitude)
        val value = coordinates[0].toString()
        textView.text = "$latitude : $value"
    }
}

@BindingAdapter(value = ["longitude"])
fun longitude(textView : TextView, coordinates: Array<Double>?) {

    coordinates?.let {
        val longitude  = textView.context.resources.getString(R.string.longitude)
        val value = coordinates[1].toString()
        textView.text = "$longitude : $value"
    }
}