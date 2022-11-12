package com.faithdeveloper.explore.retrofit

import com.faithdeveloper.explore.models.Country

object Repository {
    suspend fun getAllCountries(apiHelper: ApiHelper): List<Country> = apiHelper.getAllCountries()
    suspend fun getCountry(name: String, apiHelper: ApiHelper): List<Country> =
        apiHelper.getCountry(name)

    suspend fun getLangauge(language: String, apiHelper: ApiHelper): List<Country> =
        apiHelper.getLanguage(language)

    suspend fun getRegion(region: String, apiHelper: ApiHelper): List<Country> =
        apiHelper.getLanguage(region)

}