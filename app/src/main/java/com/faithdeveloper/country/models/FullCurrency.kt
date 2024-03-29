package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FullCurrency(
    val name:String,
    val symbol:String
) :Parcelable{
constructor():this("", "")
}
