package com.app.practice.di

import com.app.practice.BuildConfig
import com.app.practice.commons.utils.UiHelper
import com.app.practice.datasource.api.LocationApi
import com.app.practice.datasource.api.createNetworkClient
import com.app.practice.domain.CarRepository
import com.app.practice.domain.usecase.CarUseCase
import com.app.practice.vm.CarViewModel
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
            uiHelperModule)
    )
}

val viewModelModule = module {
    viewModel { CarViewModel(carUseCase = get()) }
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

val uiHelperModule = module {
    single { UiHelper(androidContext()) }
}

private val retrofit : Retrofit = createNetworkClient(BuildConfig.BASE_URL)

private val locationApi : LocationApi = retrofit.create(LocationApi::class.java)