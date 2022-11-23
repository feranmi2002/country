package com.faithdeveloper.explore.util

import com.faithdeveloper.explore.models.Country
import okhttp3.internal.checkOffsetAndCount

object Utils {
    const val PARENT = 0
    const val CHILD = 1
    const val CONTINENT = "Continent"
    const val TIME_ZONE = "Time Zone"
    const val NAME_QUERY_TYPE = "name"
    const val LANGUAGE_QUERY_TYPE = "language"
    const val CONTINENT_QUERY_TYPE = "region"
    const val SEPARATOR = "separator"

    fun sortAlphabetically(list:List<Country>): List<Country> {
        return list.sortedWith(Comparator{ country1:Country, country2:Country ->
            country1.name.official.compareTo(country2.name.official)
        })
    }
}