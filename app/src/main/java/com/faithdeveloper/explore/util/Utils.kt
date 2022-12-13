package com.faithdeveloper.explore.util

import com.faithdeveloper.explore.models.Country

object Utils {
    const val PARENT = 0
    const val CHILD = 1
    const val CONTINENT_QUERY_TYPE = "Continent"
    const val COUNTRY_QUERY_TYPE = "Country"
    const val LANGUAGE_QUERY_TYPE = "Language"
    const val CURRENCY_QUERY_TYPE = "Currency"
    const val CAPITAL_QUERY_TYPE = "Capital"
    const val DEMONYM_QUERY_TYPE = "Demonym"
    const val ALL_COUNTRIES_QUERY_TYPE = "All countries"
    const val SEPARATOR = "separator"

    fun sortAlphabetically(list:List<Country>): List<Country> {
        return list.sortedWith { country1: Country, country2: Country ->
            country1.name.official.compareTo(country2.name.official)
        }
    }
}