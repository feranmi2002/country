package com.faithdeveloper.explore.viewmodels

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.faithdeveloper.explore.paging.ExplorePagingSource
import com.faithdeveloper.explore.retrofit.ApiHelper

class ExploreViewModel(private val apiHelper: ApiHelper) : ViewModel() {
    val query = MutableLiveData<String>(null)
    var queryType: String? = null
    private val _result = query.switchMap {
        request(it, queryType)
            .liveData
            .cachedIn(viewModelScope)
    }
    val result get() = _result

    private fun request(query: String?, queryType: String?) = Pager(
        config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            ExplorePagingSource(apiHelper, query, queryType)
        }, initialKey = 1
    )

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
