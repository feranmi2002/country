package com.faithdeveloper.explore.retrofit

import com.faithdeveloper.explore.models.Country

class ApiHelper(private val apiService: ApiService) {
    suspend fun getAllCountries(): List<Country> {
        return apiService.getCountries()
    }
    suspend fun getCountry(name:String): List<Country> {
        return apiService.getCountry(name)
    }

    suspend fun getRegion(region:String):List<Country>{
        return apiService.getContinent(region)
    }

    suspend fun getLanguage(language:String):List<Country>{
        return apiService.getLanguage(language)
    }

}