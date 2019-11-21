package com.app.practice.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.practice.domain.entities.CarGenerator
import com.app.practice.domain.entities.PlaceMarks
import com.app.practice.domain.usecase.CarUseCase
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
class CarViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private var carUseCase : CarUseCase = mock()
    private lateinit var viewModel: CarViewModel

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Test
    fun verifyCarLiveDataIsNotEmptyWhenResultIsSuccess() {
        runBlocking {
            givenSuccessResult()
            whenViewModelHandleLoadCar()
            Assert.assertEquals(true, viewModel.carData.value?.isNotEmpty())
            Assert.assertEquals(1, viewModel.carData.value?.size)
        }
    }

    @Test
    fun verifyAreEmptyCarLiveData() {
        runBlocking {
            givenSuccessResult(areNecessaryEmptyCar = true)
            whenViewModelHandleLoadCar()
            Assert.assertEquals(null, viewModel.carData.value?.get(0)?.name)
        }
    }

    private fun givenSuccessResult(areNecessaryEmptyCar : Boolean = false) {
        runBlocking {
            val result: List<PlaceMarks> = if (areNecessaryEmptyCar) {
                listOf(CarGenerator.getEmptyPlaceMarksData())
            } else {
                listOf(CarGenerator.getSuccessPlaceMarksData())
            }

            given(carUseCase.execute()).willReturn(result)
        }
    }

    private fun whenViewModelHandleLoadCar() {
        viewModel = CarViewModel(carUseCase)
    }
}