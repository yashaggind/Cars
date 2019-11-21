package com.app.practice.domain.entities

object CarGenerator {

    fun getSuccessCarData(): CarApiResponse {

        return CarApiResponse(
            listOf(
                (PlaceMarks(
                    "Lesserstraße 170, 22049 Hamburg", arrayOf(10.07526, 53.59301), "CE",
                    "UNACCEPTABLE", "UNACCEPTABLE", "HH-GO8522", "WME4513341K565439"
                )),
                (PlaceMarks(
                    "Grosse Reichenstraße 7, 20457 Hamburg", arrayOf(9.99622, 53.54847), "CE",
                    "UNACCEPTABLE", "GOOD", "HH-GO8480", "WME4513341K412697"
                ))
            )
        )
    }

    fun getEmptyCarData(): CarApiResponse {
        return CarApiResponse(listOf())
    }

    fun getSuccessPlaceMarksData(): PlaceMarks {
        return PlaceMarks(
            "Grosse Reichenstraße 7, 20457 Hamburg", arrayOf(9.99622, 53.54847), "CE",
            "UNACCEPTABLE", "GOOD", "HH-GO8480", "WME4513341K412697"
        )
    }

    fun getEmptyPlaceMarksData(): PlaceMarks {
        return PlaceMarks(null, null, null, null, null, null, null)
    }
}