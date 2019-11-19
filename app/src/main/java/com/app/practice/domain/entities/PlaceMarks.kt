package com.app.practice.domain.entities

import android.os.Parcelable
import com.app.practice.commons.utils.Constants.Companion.ADDRESS_API_TAG
import com.app.practice.commons.utils.Constants.Companion.COORDINATES_API_TAG
import com.app.practice.commons.utils.Constants.Companion.ENGINE_TYPE_API_TAG
import com.app.practice.commons.utils.Constants.Companion.EXTERIOR_API_TAG
import com.app.practice.commons.utils.Constants.Companion.INTERIOR_API_TAG
import com.app.practice.commons.utils.Constants.Companion.NAME_API_TAG
import com.app.practice.commons.utils.Constants.Companion.VEHICLE_INSURANCE_API_TAG
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PlaceMarks(
    @field:Json(name = ADDRESS_API_TAG) val address: String?,
    @field:Json(name = COORDINATES_API_TAG) val coordinates: Array<Double>?,
    @field:Json(name = ENGINE_TYPE_API_TAG) val engineType: String?,
    @field:Json(name = EXTERIOR_API_TAG) val exterior: String?,
    @field:Json(name = INTERIOR_API_TAG) val interior: String?,
    @field:Json(name = NAME_API_TAG) val name: String?,
    @field:Json(name = VEHICLE_INSURANCE_API_TAG) val vehicleInsurance: String?
) : Parcelable