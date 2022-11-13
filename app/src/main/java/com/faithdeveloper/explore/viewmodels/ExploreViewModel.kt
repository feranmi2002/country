package com.faithdeveloper.explore.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.faithdeveloper.explore.util.Utils.LANGUAGE_QUERY_TYPE
import com.faithdeveloper.explore.util.Utils.NAME_QUERY_TYPE
import com.faithdeveloper.explore.paging.ExplorePagingSource
import com.faithdeveloper.explore.retrofit.ApiHelper

class ExploreViewModel(val apiHelper: ApiHelper):ViewModel() {
    val nameQuery = MutableLiveData<String>(null)
    var queryType = ""
    private val result = nameQuery.switchMap {
         allCountries(it, queryType )
            .liveData
            .cachedIn(viewModelScope)
    }
    val _result get() = result

    private fun allCountries(query:String?, queryType:String)= Pager(
        config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            when(queryType){
                NAME_QUERY_TYPE ->  ExplorePagingSource(apiHelper, query,null, null)
                LANGUAGE_QUERY_TYPE ->  ExplorePagingSource(apiHelper, null, query, null)
                else ->  ExplorePagingSource(apiHelper, null, null, query)
            }

        }, initialKey = 1
    )
}