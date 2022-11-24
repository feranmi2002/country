package com.faithdeveloper.explore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Drive(
    val signs:List<String>,
    val side:String
):Parcelable {
    constructor():this(listOf(), "")
}