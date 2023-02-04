package com.faithdeveloper.explore.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faithdeveloper.explore.models.Country
import com.faithdeveloper.explore.retrofit.ApiHelper
import com.faithdeveloper.explore.retrofit.Repository
import com.faithdeveloper.explore.util.Utils
import com.faithdeveloper.explore.util.Utils.CAPITAL_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.LANGUAGE_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.COUNTRY_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.CONTINENT_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.CURRENCY_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.DEMONYM_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.SUBCONTINENT_QUERY_TYPE
import com.faithdeveloper.explore.util.ViewModel_PagingSource_Interface
import java.util.*


class ExplorePagingSource(
    private val apiHelper: ApiHelper,
    private val query: String?,
    private val queryType: String,
    private val viewModelPagingSourceInterface: ViewModel_PagingSource_Interface
) :
    PagingSource<Int, Country>() {

    private val pageLimiter = 9
    private val pageSize = 10

    private var response: List<Country> = listOf()


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        return try {
            try {
                if (response.isEmpty()) {
                    if (params.key == 0) {
                        response = when (queryType) {
                            COUNTRY_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByName(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            CONTINENT_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByRegion(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            LANGUAGE_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByLanguage(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            CURRENCY_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByCurrency(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            CAPITAL_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByCapital(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            DEMONYM_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByDemonymn(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            SUBCONTINENT_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getBySubContinent(
                                    query!!.lowercase(Locale.ENGLISH),
                                    apiHelper
                                )
                            )
                            else -> Utils.sortAlphabetically(listOf())
//                                Utils.sortAlphabetically(Repository.getAllCountries(apiHelper))
                        }
                    }
                }
            } catch (e: retrofit2.HttpException) {
                if (e.code() != 404) throw e
            } finally {
                viewModelPagingSourceInterface.setPagerExternallyMadeEmpty(false)
                viewModelPagingSourceInterface.getResponseSize(response.size)
            }

            val result = if (params.key == 0) {
                when {
                    response.size > pageLimiter -> {
                        response.slice(IntRange(0, pageLimiter))
                    }
                    else -> {
                        val list = mutableListOf<Country>()
                        list.addAll(response)
                        list.toList()
                    }
                }
            } else {
                val start = (params.key!!).times(pageSize)
                response.slice(
                    IntRange(
                        start,
                        start + if (response.size.minus(start) > pageLimiter) pageLimiter
                        else response.size.minus(start)
                    )
                )
            }
            val currentKey = params.key
            val nextKey = if (result.size < pageSize) {
                null
            } else if (params.key != null) {
                currentKey?.plus(1)
            } else {
                null
            }
            LoadResult.Page(
                data = result,
                nextKey = nextKey,
                prevKey = null
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Country>): Int? {
        return null
    }
}