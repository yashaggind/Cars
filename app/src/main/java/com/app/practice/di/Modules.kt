package com.app.practice.di

import android.app.Application
import com.app.practice.BuildConfig
import com.app.practice.commons.helper.GoogleMapHelper
import com.app.practice.commons.helper.UiHelper
import com.app.practice.datasource.api.LocationApi
import com.app.practice.datasource.api.createNetworkClient
import com.app.practice.domain.CarRepository
import com.app.practice.domain.usecase.CarUseCase
import com.app.practice.vm.CarViewModel
import com.app.practice.vm.LocationViewModel
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import retrofit2.Retrofit

fun injectFeature() = loadFeature

private val loadFeature by lazy {
    loadKoinModules(
        listOf(viewModelModule,
            repositoryModule,
            networkModule,
            useCaseModule,
            uiHelperModule,
            fUsedLocationModule,
            googleMapModule)
    )
}

val viewModelModule = module {
    viewModel { CarViewModel(carUseCase = get()) }
    viewModel { LocationViewModel(locationProviderClient = get(),uiHelper = get()) }
}

val repositoryModule = module {
    single { CarRepository(locationApi = get()) }
}

val useCaseModule = module {
    single { CarUseCase(carRepository = get()) }
}

val networkModule = module {
    single { locationApi }
}

val fUsedLocationModule = module {
    single { locationProviderClient(androidApplication()) }
}

val googleMapModule = module {
    single { GoogleMapHelper() }
}

val uiHelperModule = module {
    single { UiHelper(androidContext()) }
}

private val retrofit : Retrofit = createNetworkClient(BuildConfig.BASE_URL)

private val locationApi : LocationApi = retrofit.create(LocationApi::class.java)

private fun locationProviderClient(androidApplication : Application)
        = LocationServices.getFusedLocationProviderClient(androidApplication)