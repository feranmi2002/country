package com.faithdeveloper.explore.retrofit

import com.faithdeveloper.explore.models.Country
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("all")
    suspend fun getCountries(): List<Country>

    @GET("name/{name}")
    suspend fun getCountry(
        @Path("name") name:String
    ):List<Country>

    @GET("lang/{lang}")
    suspend fun getLanguage(
        @Path("lang") lang:String
    ):List<Country>

    @GET("lang/{region}")
    suspend fun getContinent(
        @Path("region") continent:String
    ):List<Country>
}