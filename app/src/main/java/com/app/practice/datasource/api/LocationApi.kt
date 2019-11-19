package com.app.practice.datasource.api

import com.app.practice.commons.utils.Constants.Companion.LOCATION_API_TAG
import com.app.practice.domain.entities.CarApiResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface LocationApi {
    @GET(LOCATION_API_TAG)
    fun getLocationAsync() : Deferred<Response<CarApiResponse>>
}