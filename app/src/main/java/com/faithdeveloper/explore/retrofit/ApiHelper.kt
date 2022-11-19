package com.faithdeveloper.explore.retrofit

import com.faithdeveloper.explore.models.Country

class ApiHelper(private val apiService: ApiService) {
    suspend fun getAllCountries(): List<Country> {
        return apiService.getCountries()
    }
    suspend fun getACountry(name:String): List<Country> {
        return apiService.getACountry(name)
    }

    suspend fun getByRegion(region:String):List<Country>{
        return apiService.getByContinent(region)
    }

    suspend fun getByLanguage(language:String):List<Country>{
        return apiService.getByLanguage(language)
    }

}