package com.faithdeveloper.explore.models

import com.faithdeveloper.explore.util.Utils
import com.faithdeveloper.explore.util.Utils.PARENT

data class FilterHeader(
    val title: String,
    var type: Int = Utils.PARENT,
    var child: FilterChild,
    var isExpanded: Boolean = false
) {
    constructor() : this("", PARENT, FilterChild(), false)
}

