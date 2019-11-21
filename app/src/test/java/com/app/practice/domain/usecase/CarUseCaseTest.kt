package com.app.practice.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.practice.commons.utils.ConstantTest.Companion.EXPECTED_ERROR_RESPONSE_CODE
import com.app.practice.commons.utils.ConstantTest.Companion.EXPECTED_SUCCESS_RESPONSE_CODE
import com.app.practice.datasource.api.NetworkState
import com.app.practice.domain.CarRepository
import com.app.practice.domain.entities.CarGenerator
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class CarUseCaseTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val carRepository: CarRepository = mock()
    private val carUseCase = CarUseCase(carRepository)

    @Test
    fun verifyResultWhenRepoMockReturnSuccessState() {

        runBlocking {

            val result = Response.success(CarGenerator.getEmptyCarData())
            given(carRepository.getLocation())
                .willReturn(result)

            val realResult = carUseCase.execute()
            val expectedResult = Response.success(CarGenerator.getEmptyCarData().placeMarks)

            Assert.assertEquals(expectedResult.body(), realResult)
        }
    }

    @Test
    fun verifyUseCaseCallRepository() {

        val response = Response.success(CarGenerator.getEmptyCarData())

        runBlocking {
            given(carRepository.getLocation())
                .willReturn(response)

            carUseCase.execute()

            verify(carRepository, times(1)).getLocation()
        }
    }

    @Test
    fun verifyIsLoadingLiveDataWhenResultIsSuccess() {
        val expectedResult = 200
        carUseCase.getNetworkState().value = NetworkState.Success(expectedResult)
        Assert.assertEquals(
            (carUseCase.getNetworkState().value as NetworkState.Success<Int>).code,
            EXPECTED_SUCCESS_RESPONSE_CODE
        )
    }

    @Test
    fun verifyIsLoadingLiveDataWhenResultIsError() {
        val expectedResult = 404
        carUseCase.getNetworkState().value = NetworkState.Error(expectedResult)
        Assert.assertEquals(
            (carUseCase.getNetworkState().value as NetworkState.Error<Int>).code,
            EXPECTED_ERROR_RESPONSE_CODE
        )
    }
}