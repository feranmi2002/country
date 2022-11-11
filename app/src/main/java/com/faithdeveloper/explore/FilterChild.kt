package com.faithdeveloper.explore

data class FilterChild(
                       var type: String,
                       var data: MutableList<String>
){
    constructor():this("", mutableListOf())
}
