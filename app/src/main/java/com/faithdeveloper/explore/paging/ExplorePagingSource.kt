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
import com.faithdeveloper.explore.util.ViewModel_PagingSource_Interface
import java.util.*

class ExplorePagingSource(
    private val apiHelper: ApiHelper,
    private val query: String?,
    private val queryType: String,
    private val viewModelPagingSourceInterface: ViewModel_PagingSource_Interface
) :
    PagingSource<Int, Country>() {
    private var response: List<Country> = listOf()


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        return try {
            try {
                if (response.isEmpty()) {
                    if (params.key == 1) {
                        response = when (queryType) {
                            COUNTRY_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByName(
                                    query!!.lowercase(Locale.getDefault()),
                                    apiHelper
                                )
                            )
                            CONTINENT_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByRegion(
                                    query!!.lowercase(Locale.getDefault()),
                                    apiHelper
                                )
                            )
                            LANGUAGE_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByLanguage(
                                    query!!.lowercase(Locale.getDefault()),
                                    apiHelper
                                )
                            )
                            CURRENCY_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByCurrency(
                                    query!!.lowercase(Locale.getDefault()),
                                    apiHelper
                                )
                            )
                            CAPITAL_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByCapital(
                                    query!!.lowercase(Locale.getDefault()),
                                    apiHelper
                                )
                            )
                            DEMONYM_QUERY_TYPE -> Utils.sortAlphabetically(
                                Repository.getByDemonymn(
                                    query!!.lowercase(Locale.getDefault()),
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
                viewModelPagingSourceInterface.getResponseSize(response.size)
            }
            val result = if (params.key == 1) {
                when {
                    response.size >= 9 -> {
                        response.slice(IntRange(0, 9))
                    }
                    else -> {
                        val list = mutableListOf<Country>()
                        list.addAll(response)
                        list.toList()
                    }
                }
            } else response.slice(
                IntRange(
                    params.key!!.times(10),
                    (params.key!! * 10) + 9
                )
            )
            val currentKey = params.key
            val nextKey = if (result.size < 10) {
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