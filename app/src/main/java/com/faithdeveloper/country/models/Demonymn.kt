package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Demonymn(
    val eng:DemonymnEnglish
):Parcelable{
    constructor():this(DemonymnEnglish())
}
