package com.faithdeveloper.explore

object Repository {
    suspend fun getAllCountries(apiHelper: ApiHelper):List<Country> = apiHelper.getAllCountries()
    suspend fun getCountry(name:String, apiHelper: ApiHelper):List<Country> = apiHelper.getCountry(name)
}