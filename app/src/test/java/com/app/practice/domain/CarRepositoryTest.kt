package com.app.practice.domain

import com.app.practice.commons.utils.ConstantTest.Companion.CAR_ADDRESS
import com.app.practice.commons.utils.ConstantTest.Companion.CAR_INTERIOR
import com.app.practice.commons.utils.ConstantTest.Companion.CAR_LATITUDE
import com.app.practice.commons.utils.ConstantTest.Companion.CAR_LONGITUDE
import com.app.practice.commons.utils.ConstantTest.Companion.CAR_NAME
import com.app.practice.commons.utils.ConstantTest.Companion.CAR_VEHICLE_NUMBER
import com.app.practice.commons.utils.SequenceList
import com.app.practice.domain.entities.CarGenerator
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import retrofit2.Response

@RunWith(JUnit4::class)
class CarRepositoryTest {

    private val carRepository: CarRepository = mock()

    @Test
    fun verifyApiModelToEntityModelMustReturnSameValues() {

        val response = Response.success(CarGenerator.getSuccessCarData())

        runBlocking {
            Mockito.`when`(carRepository.getLocation()).thenAnswer(SequenceList(listOf(response)))
        }

        Assert.assertEquals(2, response.body()?.placeMarks?.size)
        Assert.assertEquals(CAR_ADDRESS, response.body()?.placeMarks?.get(0)?.address)
        Assert.assertTrue(response.body()?.placeMarks?.get(0)?.coordinates?.get(0) == CAR_LONGITUDE)
        Assert.assertTrue(response.body()?.placeMarks?.get(0)?.coordinates?.get(1) == CAR_LATITUDE)
        Assert.assertNotNull("", response.body()?.placeMarks?.get(1)?.address)
        Assert.assertFalse(response.body()?.placeMarks?.get(0)?.name == CAR_NAME)
        Assert.assertFalse(response.body()?.placeMarks?.get(0)?.vehicleInsurance == CAR_VEHICLE_NUMBER)
        Assert.assertTrue(response.body()?.placeMarks?.get(1)?.interior == CAR_INTERIOR)
    }

    @Test
    fun verifyResultWhenRepoMockReturnEmptyState() {

        val response = Response.success(CarGenerator.getEmptyCarData())

        runBlocking {
            Mockito.`when`(carRepository.getLocation()).thenAnswer(SequenceList(listOf(response)))
        }

        val expectedResult = Response.success(CarGenerator.getEmptyCarData())

        Assert.assertEquals(expectedResult.body(), response.body())

        Assert.assertEquals(0, response.body()?.placeMarks?.size)
        response.body()?.placeMarks?.isNullOrEmpty()?.let { Assert.assertTrue(it) }
        response.body()?.placeMarks?.isNotEmpty()?.let { Assert.assertFalse(it) }
    }
}