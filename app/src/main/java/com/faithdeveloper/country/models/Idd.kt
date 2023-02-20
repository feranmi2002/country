package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Idd(
    val root:String,
    val suffixes:List<String>
):Parcelable
{
    constructor():this("", listOf())
}
