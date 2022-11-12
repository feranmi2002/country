package com.faithdeveloper.explore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Name(
    val official:String
):Parcelable{
    constructor(): this("")
}
