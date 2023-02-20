package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Maps(
    val googleMaps:String,
    val openStreetMaps:String
):Parcelable
{
    constructor():this("", "")
}
