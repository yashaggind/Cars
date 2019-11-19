package com.app.practice.domain

import com.app.practice.datasource.api.LocationApi

class CarRepository(private val locationApi: LocationApi) {

    suspend fun getLocation() = locationApi.getLocationAsync().await()
}