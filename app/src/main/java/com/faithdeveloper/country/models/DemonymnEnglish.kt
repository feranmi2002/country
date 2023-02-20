package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DemonymnEnglish(
    val f:String,
    val m:String,
):Parcelable{
    constructor():this("", "")
}
