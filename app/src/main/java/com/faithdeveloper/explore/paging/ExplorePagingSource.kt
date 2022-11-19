package com.faithdeveloper.explore.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faithdeveloper.explore.models.Country
import com.faithdeveloper.explore.retrofit.ApiHelper
import com.faithdeveloper.explore.retrofit.Repository
import com.faithdeveloper.explore.util.Utils
import com.faithdeveloper.explore.util.Utils.LANGUAGE_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.NAME_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.REGION_QUERY_TYPE

class ExplorePagingSource(
    private val apiHelper: ApiHelper,
    private var query: String?,
    private var queryType: String?,
) :
    PagingSource<Int, Country>() {
    private var response: List<Country> = listOf()


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        return try {
            if (response.isEmpty()) {
                if (params.key == 1) {
                    response = when (queryType) {
                        NAME_QUERY_TYPE -> Utils.sortAlphabetically(
                            Repository.getByName(
                                query!!,
                                apiHelper
                            )
                        )
                        REGION_QUERY_TYPE -> Utils.sortAlphabetically(
                            Repository.getByRegion(
                                query!!,
                                apiHelper
                            )
                        )
                        LANGUAGE_QUERY_TYPE -> Utils.sortAlphabetically(
                            Repository.getByRegion(
                                query!!,
                                apiHelper
                            )
                        )
                        else -> Utils.sortAlphabetically(Repository.getAllCountries(apiHelper))
                    }
                }
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
            val nextKey = when {
                params.key != null -> {
                    currentKey?.plus(1)
                }
                result.size < 20 -> {
                    null
                }
                else -> {
                    null
                }
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