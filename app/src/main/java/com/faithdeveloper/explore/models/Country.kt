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
    val unMember:Boolean,
    val subregion:String,
    val latlng:List<Double>,
    val landlocked:Boolean,
    val maps:Maps,
    val startOfWeek:String,
    val car:Drive


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
        false,
        "",
        listOf(),
        false,
        Maps(),
        "",
        Drive()
    )
}