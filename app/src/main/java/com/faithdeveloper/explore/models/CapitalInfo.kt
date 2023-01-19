package com.faithdeveloper.explore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CapitalInfo(
    val latlng:List<Double>
):Parcelable{
    constructor():this(listOf())
}
