package com.faithdeveloper.explore.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.faithdeveloper.explore.paging.ExplorePagingSource
import com.faithdeveloper.explore.retrofit.ApiHelper
import com.faithdeveloper.explore.util.Utils.ALL_COUNTRIES_QUERY_TYPE
import com.faithdeveloper.explore.util.ViewModel_PagingSource_Interface

class ExploreViewModel(private val apiHelper: ApiHelper) : ViewModel(), ViewModel_PagingSource_Interface {
    val query = MutableLiveData<String>(null)
    var queryType: String = ALL_COUNTRIES_QUERY_TYPE
    private val _result = query.switchMap {
        request(it, queryType)
            .liveData
            .cachedIn(viewModelScope)
    }
    val result get() = _result
    private var _responseSize = 0
    val responseSize get() = _responseSize

    private fun request(query: String?, queryType: String) = Pager(
        config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            ExplorePagingSource(apiHelper, query, queryType,this)
        }, initialKey = 1
    )

    override fun getResponseSize(responseSize: Int) {
        this._responseSize = responseSize
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun factory(apiHelper: ApiHelper): ViewModelProvider.Factory {
            val provider = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ExploreViewModel::class.java))
                        return (ExploreViewModel(apiHelper)) as T
                    else throw IllegalArgumentException("Unknown class")
                }
            }
            return provider
        }
    }
}
