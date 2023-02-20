package com.faithdeveloper.country.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    val name: Name,
    val tld:List<String>,
    val region: String,
    val capital: List<String>,
    val languages: Language,
    val currencies: Currency,
    val idd:Idd,
    val borders:List<String>,
    val altSpellings:List<String>,
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
    val car:Drive,
    val flag:String,
    val demonyms:Demonymn,
    val continents:List<String>,
    val fifa:String,
    val population:Long,
    val capitalInfo:CapitalInfo


) : Parcelable {
    constructor() : this(
        Name(),
        listOf(),
        "",
        listOf(),
        Language(),
        Currency(),
        Idd(),
        listOf(),
        listOf(),
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
        Drive(),
        "",
        Demonymn(),
        listOf(),
        "",
        0,
        CapitalInfo()
    )
}