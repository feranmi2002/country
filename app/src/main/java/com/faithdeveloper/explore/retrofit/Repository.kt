package com.faithdeveloper.explore.retrofit

import com.faithdeveloper.explore.models.Country

object Repository {
    suspend fun getAllCountries(apiHelper: ApiHelper): List<Country> = apiHelper.getAllCountries()
    suspend fun getByName(name: String, apiHelper: ApiHelper): List<Country> =
        apiHelper.getACountry(name)

    suspend fun getByLanguage(language: String, apiHelper: ApiHelper): List<Country> =
        apiHelper.getByLanguage(language)

    suspend fun getByRegion(region: String, apiHelper: ApiHelper): List<Country> =
        apiHelper.getByRegion(region)

}