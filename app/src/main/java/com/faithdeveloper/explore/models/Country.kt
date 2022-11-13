package com.faithdeveloper.explore.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    val name: Name,
    val region: String,
    val capital: List<String>,
    val motto: String,
    val languages: Language,
    val currencies: Currency,
    val area: Double,
    val independent: Boolean,
    val flags: Image,
    val timezones: List<String>,
    val coatOfArms: Image,
    val type:String,
    val separatorText:String
) : Parcelable {
    constructor() : this(
        Name(),
        "",
        listOf(),
        "",
        Language(),
        Currency(),
        Double.MIN_VALUE,
        true,
        Image(),
        mutableListOf(),
        Image(),
        "",
        ""
    )
}