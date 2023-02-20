package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    val shortName: FullCurrency
):Parcelable{
    constructor(): this(FullCurrency())
}
