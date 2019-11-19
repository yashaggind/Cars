package com.app.practice.domain.entities

import android.os.Parcelable
import com.app.practice.commons.utils.Constants.Companion.PLACE_MARKS_API_TAG
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CarApiResponse(
    @field:Json(name = PLACE_MARKS_API_TAG) val placeMarks : List<PlaceMarks>
) : Parcelable