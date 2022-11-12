package com.faithdeveloper.explore

data class Image(
    val svg:String,
    val png:String
){
    constructor():this("", "")
}
