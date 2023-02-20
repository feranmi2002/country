package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Name(
    val common:String,
    val official:String
):Parcelable{
    constructor(): this("", "")
}
