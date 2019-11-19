package com.app.practice.datasource.api

sealed class NetworkState<T>(val code : Int? = null) {
    class Success<T>(code : Int) : NetworkState<T>(code)
    class Loading<T> : NetworkState<T>()
    class Error<T>(code : Int) : NetworkState<T>(code)
}
