package com.faithdeveloper.explore

import androidx.paging.PagingSource
import androidx.paging.PagingState

class ExplorePagingSource(val apiHelper: ApiHelper, val name:String?) : PagingSource<Int, Country>() {
    var response: List<Country>? = null


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Country> {
        return try {
            if (response == null) {
                if (params.key == 1) {
                    if (name == null) {
                        response = Repository.getAllCountries(apiHelper)
                    } else {
                        response = Repository.getCountry(name, apiHelper)
                    }
                }
            }
            val result = if (params?.key == 1) response?.slice(IntRange(0, 9))
            else response?.slice(
                IntRange(
                    params.key!!.times(10),
                    (params.key!! * 10) + 9)
            )
            val currentKey = params.key
            val nextKey = if (params.key != null){
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