package com.faithdeveloper.explore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Currency(
    val shortName:FullCurrency
):Parcelable{
    constructor(): this(FullCurrency())
}
