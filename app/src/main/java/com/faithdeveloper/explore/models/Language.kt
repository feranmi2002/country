package com.faithdeveloper.explore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Language(
    val language:String
):Parcelable{
    constructor():this("")
}
