package com.faithdeveloper.explore.util

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

interface FilterInterface {
    fun filter(filter:String, type:String)
}