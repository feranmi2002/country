package com.faithdeveloper.explore.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.faithdeveloper.explore.models.Country
import com.faithdeveloper.explore.retrofit.ApiHelper
import com.faithdeveloper.explore.retrofit.Repository
import com.faithdeveloper.explore.util.Utils

class ExplorePagingSource(
    val apiHelper: ApiHelper,
    var name: String?,
    var language: String?,
    var region: String?
) :
    PagingSource<Int, Country>() {
    var response: List<Country> = listOf()


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        return try {
            if (response.isEmpty()) {
                if (params.key == 1) {
                    response = if (name == null) {
                        Utils.sortAlphabetically(Repository.getAllCountries(apiHelper))
                    } else {
                        Repository.getCountry(name!!, apiHelper)
                    }
                }
            } else {
                if (name != null) {
                    response = Utils.sortAlphabetically(Repository.getCountry(name!!, apiHelper))
                    name = null
                } else if (language != null) {
                    response =
                        Utils.sortAlphabetically(Repository.getLangauge(language!!, apiHelper))
                    language = null
                } else if (region != null) {
                    response = Utils.sortAlphabetically(Repository.getRegion(region!!, apiHelper))
                    region = null
                }
            }

            val result = if (params?.key == 1) {
                if (response.size >= 9) {
                    response?.slice(IntRange(0, 9))
                } else {
                    val list = mutableListOf<Country>()
                    list.addAll(response)
                    list.toList()
                }
            } else response?.slice(
                IntRange(
                    params.key!!.times(10),
                    (params.key!! * 10) + 9
                )
            )
            val currentKey = params.key
            val nextKey = if (params.key != null) {
                currentKey?.plus(1)
            } else if (result?.size!! < 20) {
                null
            } else {
                null
            }
            LoadResult.Page(
                data = result!!,
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