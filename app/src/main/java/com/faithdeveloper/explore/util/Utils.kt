package com.faithdeveloper.explore.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.faithdeveloper.explore.R
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
    const val SUBCONTINENT_QUERY_TYPE = "Sub-Continent"
    const val ALL_COUNTRIES_QUERY_TYPE = "All countries"
    const val SEPARATOR = "separator"


    fun emptyResultFeedback(context: Context, query_type: String) = when (query_type) {
        COUNTRY_QUERY_TYPE -> context.resources.getString(R.string.countries_empty)
        CONTINENT_QUERY_TYPE -> context.resources.getString(R.string.continents_empty)
        LANGUAGE_QUERY_TYPE -> context.resources.getString(R.string.language_empty)
        CURRENCY_QUERY_TYPE -> context.resources.getString(R.string.currency_empty)
        CAPITAL_QUERY_TYPE -> context.resources.getString(R.string.capital_empty)
        DEMONYM_QUERY_TYPE -> context.resources.getString(R.string.demonymn_empty)
        ALL_COUNTRIES_QUERY_TYPE -> context.resources.getString(R.string.all_countries_empty)
        else -> {
            context.resources.getString(R.string.subcontinent_empty)
        }
    }

    fun infoResource(context: Context, query_type: String) = when(query_type){
        CONTINENT_QUERY_TYPE -> context.resources.getString(R.string.continents_info)
        LANGUAGE_QUERY_TYPE -> context.resources.getString(R.string.language_info)
        CURRENCY_QUERY_TYPE -> context.resources.getString(R.string.currency_info)
        COUNTRY_QUERY_TYPE -> context.resources.getString(R.string.countries_info)
        CAPITAL_QUERY_TYPE -> context.resources.getString(R.string.capital_info)
        DEMONYM_QUERY_TYPE -> context.resources.getString(R.string.demonymn_info)
        ALL_COUNTRIES_QUERY_TYPE -> context.resources.getString(R.string.all_countries_info)
        else -> {
            context.resources.getString(R.string.subcontinent_info)
        }
    }

    fun hideKeyboard(rootView: View,context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            rootView.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }
    fun showKeyboard(rootView: View, context: Context){
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(rootView, InputMethodManager.RESULT_UNCHANGED_SHOWN)

    }

    fun sortAlphabetically(list: List<Country>): List<Country> {
        return list.sortedWith { country1: Country, country2: Country ->
            country1.name.official.compareTo(country2.name.official)
        }
    }
}