package com.app.practice.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.app.practice.datasource.api.NetworkState
import com.app.practice.domain.CarRepository
import com.app.practice.domain.entities.PlaceMarks

class CarUseCase(private val carRepository: CarRepository){

    // FOR DATA ---
    private val networkState = MutableLiveData<NetworkState<Int>>()
    
    suspend fun execute(): List<PlaceMarks> {

        networkState.postValue(NetworkState.Loading())

        var data : List<PlaceMarks> = listOf()

        val response = carRepository.getLocation()

        if(response.isSuccessful) {
            val items = response.body()?.placeMarks

            if(items?.size!! >= 0) data = items

            networkState.postValue(NetworkState.Success(response.code()))
        }
        else networkState.postValue(NetworkState.Error(response.code()))

        return data
    }

    fun getNetworkState(): MutableLiveData<NetworkState<Int>> = networkState
}