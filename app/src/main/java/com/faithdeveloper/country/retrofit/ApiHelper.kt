package com.faithdeveloper.country.retrofit

import com.faithdeveloper.country.models.Country

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

    suspend fun getByCurrency(currency:String):List<Country>{
        return apiService.getByCurrency(currency)
    }
    suspend fun getByCapital(capital:String):List<Country>{
        return apiService.getByCapital(capital)
    }
    suspend fun getByDemonym(demonymn:String):List<Country>{
        return apiService.getByDemonym(demonymn)
    }
    suspend fun getBySubContinent(subregion:String):List<Country>{
        return apiService.getBySubContinent(subregion)
    }

}