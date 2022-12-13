package com.faithdeveloper.explore.retrofit

import com.faithdeveloper.explore.models.Country
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("all")
    suspend fun getCountries(): List<Country>

    @GET("name/{name}")
    suspend fun getACountry(
        @Path("name") name:String
    ):List<Country>

    @GET("lang/{lang}")
    suspend fun getByLanguage(
        @Path("lang") lang:String
    ):List<Country>

    @GET("region/{region}")
    suspend fun getByContinent(
        @Path("region") continent:String
    ):List<Country>

    @GET("currency/{currency}")
    suspend fun getByCurrency(
        @Path("currency") currency:String
    ):List<Country>

    @GET("capital/{capital}")
    suspend fun getByCapital(
        @Path("capital") capital:String
    ):List<Country>

    @GET("demonym/{demonym}")
    suspend fun getByDemonym(
        @Path("demonym") demonym:String
    ):List<Country>

}