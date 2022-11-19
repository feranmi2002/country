package com.faithdeveloper.explore.models

data class FilterGrandChild(
    var title:String,
    var checked:Boolean = false
){
    constructor(): this("", false)
}
