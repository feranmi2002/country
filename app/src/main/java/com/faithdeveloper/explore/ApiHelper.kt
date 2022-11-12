package com.faithdeveloper.explore

class ApiHelper(private val apiService: ApiService) {
    suspend fun getAllCountries(): List<Country> {
        return apiService.getCountries()
    }
    suspend fun getCountry(name:String): List<Country> {
        return apiService.getCountry(name)
    }
}