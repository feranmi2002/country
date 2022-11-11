package com.faithdeveloper.explore

import com.faithdeveloper.explore.Utils.PARENT

data class FilterHeader(
    val title: String,
    var type: Int = Utils.PARENT,
    var child: FilterChild,
    var isExpanded: Boolean = false
) {
    constructor() : this("", PARENT, FilterChild(), false)
}

