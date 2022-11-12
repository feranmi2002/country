package com.faithdeveloper.explore

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData

class ExploreViewModel(val apiHelper: ApiHelper):ViewModel() {
    val nameQuery = MutableLiveData<String>(null)
    private val result = nameQuery.switchMap {
        allCountries(it).cachedIn(viewModelScope)
    }
    val _result get() = result

    private fun allCountries(name:String?)= Pager(
        config = PagingConfig(pageSize = 10), pagingSourceFactory = {
                ExplorePagingSource(apiHelper, name)
        }, initialKey = 1
    ).liveData
}