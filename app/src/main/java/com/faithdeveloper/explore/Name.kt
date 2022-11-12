package com.faithdeveloper.explore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Name(
    val official:String
):Parcelable{
    constructor(): this("")
}
