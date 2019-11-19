package com.app.practice.vm

import androidx.lifecycle.MutableLiveData
import com.app.practice.commons.base.BaseViewModel
import com.app.practice.datasource.api.NetworkState
import com.app.practice.domain.entities.PlaceMarks
import com.app.practice.domain.usecase.CarUseCase
import kotlinx.coroutines.launch

class CarViewModel(private val carUseCase: CarUseCase): BaseViewModel() {
    // OBSERVABLES ---
    var networkState : MutableLiveData<NetworkState<Int>> = MutableLiveData()
    val carData : MutableLiveData<List<PlaceMarks>> = MutableLiveData()

    // UTILS ---
    init {
        handleCategoryLoad()
    }

    private fun handleCategoryLoad() {
        ioScope.launch { updateCategoryLiveData(carUseCase.execute()) }
        networkState = carUseCase.getNetworkState()
    }

    private fun updateCategoryLiveData(result: List<PlaceMarks>)
            = carData.postValue(result)
}