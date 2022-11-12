package com.faithdeveloper.explore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val svg:String,
    val png:String
):Parcelable{
    constructor():this("", "")
}
