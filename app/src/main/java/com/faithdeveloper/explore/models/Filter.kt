package com.faithdeveloper.explore.models

data class Filter(
    var title:String,
    var checked:Boolean = false
){
    constructor(): this("", false)
}
