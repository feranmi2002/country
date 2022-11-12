package com.faithdeveloper.explore

data class Currency(
    val shortName:FullCurrency
){
    constructor(): this(FullCurrency())
}
