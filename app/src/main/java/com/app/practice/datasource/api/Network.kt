package com.app.practice.datasource.api

import com.app.practice.commons.utils.Constants.Companion.TIMEOUT_REQUEST
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

fun createNetworkClient(baseUrl: String) = retrofitClient(baseUrl, httpClient())

private fun httpClient(): OkHttpClient =
    OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_REQUEST, TimeUnit.SECONDS).build()

private fun retrofitClient(baseUrl: String, httpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
