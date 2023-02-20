package com.faithdeveloper.country.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.faithdeveloper.country.R
import com.faithdeveloper.country.models.Country


object Utils {
    const val CONTINENT_QUERY_TYPE = "Continent"
    const val COUNTRY_QUERY_TYPE = "Country"
    const val LANGUAGE_QUERY_TYPE = "Language"
    const val CURRENCY_QUERY_TYPE = "Currency"
    const val CAPITAL_QUERY_TYPE = "Capital"
    const val DEMONYM_QUERY_TYPE = "Demonym"
    const val SUBCONTINENT_QUERY_TYPE = "Sub-Continent"
    const val CAPITAL = "capital"
    const val ALL_COUNTRIES_QUERY_TYPE = "All countries"
    const val NAME_COMMON = "name_common"
    const val NAME_OFFICIAL = "name_official"
    const val TLD = "tld"
    const val REGION = "region"
    const val LANGUAGES = "languages"
    const val MOTTO = "motto"
    const val IDD = "idd"
    const val BORDERS = "borders"
    const val ALT_SPELLINGS = "altSpellings"
    const val AREA = "area"
    const val INDEPENDENT = "independent"
    const val FLAGS = "flags"
    const val TIMEZONES = "timezones"
    const val COAT_OF_ARMS = "coatOfArms"
    const val UN_MEMBER = "unMember"
    const val SUBREGION = "subregion"
    const val LATLNG = "latlng"
    const val LANDLOCKED = "landlocked"
    const val GOOGLE_MAPS = "google_maps"
    const val OPEN_STREET_MAPS = "open_street_maps"
    const val START_OF_WEEK = "start_of_week"
    const val DRIVING_SIDE = "driving_side"
    const val FLAG = "flag"
    const val MALE_DEMONYMNS = "male_demonymns"
    const val FEMALE_DEMONYMNS = "female_demonymns"
    const val CONTINENTS = "continents"
    const val FIFA = "fifa"
    const val POPULATION = "population"
    const val CAPITAL_INFO = "capital_info"


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

    fun infoResource(context: Context, query_type: String) = when (query_type) {
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

    fun hideKeyboard(rootView: View, context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            rootView.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN
        )
    }

    fun showKeyboard(rootView: View, context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(rootView, InputMethodManager.RESULT_UNCHANGED_SHOWN)

    }

    fun sortAlphabetically(list: List<Country>): List<Country> {
        return list.sortedWith { country1: Country, country2: Country ->
            country1.name.official.compareTo(country2.name.official)
        }
    }

    fun formatCountryProperties(
        country: Country,
        abbreviations: Array<String>,
        countries: Array<String>
    ): List<String> {
        val map = mutableMapOf<String, String>(
            NAME_OFFICIAL to country.name.official,
            NAME_COMMON to country.name.common,
            ALT_SPELLINGS to formatListOfStrings(country.altSpellings),
            LATLNG to formatListOfStrings(country.latlng.map { it.toString() }),
            CAPITAL to formatListOfStrings(country.capital),
            CAPITAL_INFO to formatListOfStrings(country.capitalInfo.latlng.map { it.toString() }),
            MALE_DEMONYMNS to country.demonyms.eng.m,
            FEMALE_DEMONYMNS to country.demonyms.eng.f,
            AREA to country.area.toString(),
            TIMEZONES to formatListOfStrings(country.timezones),
            INDEPENDENT to country.independent.toString().capitalizer(),
            REGION to country.region,
            SUBREGION to country.subregion,
            CONTINENTS to formatListOfStrings(country.continents),
            POPULATION to country.population.toString(),
            BORDERS to formatBorders(country.borders, abbreviations, countries),
            LANDLOCKED to country.landlocked.toString().capitalizer(),
            IDD to formatListOfStrings(formatIdd(country.idd.root, country.idd.suffixes)),
            TLD to formatListOfStrings(country.tld),
            GOOGLE_MAPS to country.maps.googleMaps,
            OPEN_STREET_MAPS to country.maps.openStreetMaps,
            UN_MEMBER to country.unMember.toString().capitalizer(),
            FIFA to country.fifa,
            START_OF_WEEK to country.startOfWeek.capitalizer(),
            DRIVING_SIDE to country.car.side.capitalizer(),
            FLAG to country.flag,
            LANGUAGES to country.languages.language,
            COAT_OF_ARMS to country.coatOfArms.png
//            "currencies" to country.currencies.shortName.name
        )
        return map.map {
            it.value
        }

    }

    private fun formatIdd(idd: String, suffixes: List<String>) = suffixes.map { suffix ->
        idd + suffix

    }

    private fun formatListOfStrings(list: List<String?>): String {
        var string = ""
        var size = list.size
        list.forEach {
            it?.let {
                string += it + if (size > 1) ", "
                else ""
                size -= 1
            }
        }
        return string
    }

    private fun formatBorders(
        list: List<String>,
        abbreviations: Array<String>,
        countries: Array<String>
    ): String {
        val mapOfStrings = mutableMapOf<String, String>()
        abbreviations.onEachIndexed { index, abbreviation ->
            mapOfStrings[abbreviation] = countries[index]
        }
        val mCountries = list.map {
            mapOfStrings[it]
        }
        return formatListOfStrings(mCountries)
    }

    fun String.capitalizer() = this.capitalize()


}