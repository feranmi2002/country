package com.faithdeveloper.explore

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("all")
    suspend fun getCountries(): List<Country>

    @GET("name/{name}")
    suspend fun getCountry(
        @Path("name") name:String
    ):List<Country>
}