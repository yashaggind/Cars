package com.app.practice.domain.usecase

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.practice.datasource.api.NetworkState
import com.app.practice.domain.CarRepository
import com.app.practice.domain.entities.PlaceMarks

class CarUseCase(private val carRepository: CarRepository){

    // FOR DATA ---
    private val networkState = MutableLiveData<NetworkState<Int>>()

    private val TAG : String = "CarActivity"

    suspend fun execute(): List<PlaceMarks> {

        networkState.postValue(NetworkState.Loading())

        var data : List<PlaceMarks> = listOf()

        val response = carRepository.getLocation()

        Log.e(TAG,"CarUseCase response isSuccessful: ${response.isSuccessful}")
        Log.e(TAG,"CarUseCase response code: ${response.code()}")
        Log.e(TAG,"CarUseCase response message: ${response.message()}")

        if(response.isSuccessful) {
            val items = response.body()?.placeMarks

            Log.e(TAG,"CarUseCase response: $items")
            Log.e(TAG,"CarUseCase response size: ${items?.size}")

            if(items?.size!! >= 0) data = items

            networkState.postValue(NetworkState.Success(response.code()))
        }
        else networkState.postValue(NetworkState.Error(response.code()))

        return data
    }

    fun getNetworkState(): MutableLiveData<NetworkState<Int>> = networkState
}