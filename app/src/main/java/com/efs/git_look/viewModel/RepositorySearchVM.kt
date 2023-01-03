package com.efs.git_look.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.efs.git_look.model.RepositoriesSource
import com.efs.git_look.model.Repository
import com.efs.git_look.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepositorySearchVM: ViewModel() {
    /*var firstScrollIndex = mutableStateOf(0)
    var firstScrollOffset = mutableStateOf(0)*/
    private val  _isRefreshing = MutableStateFlow(false)
    /*val isRefreshing = _isRefreshing.asStateFlow()*/

    /*private val _query = MutableStateFlow("")
    val query = _query.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ""
    )
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false,
    )*/

    var query = mutableStateOf("")
    var isQuerying = mutableStateOf(false)
    var repository: Flow<PagingData<Repository>> = Pager(PagingConfig(pageSize = 10)){
        RepositoriesSource(query.value)
    }.flow.cachedIn(viewModelScope)

    fun refresh()= viewModelScope.launch{
        _isRefreshing.update { true }
        delay(2000)
        _isRefreshing.update { false }
        repository = Pager(PagingConfig(pageSize = 20)){
            RepositoriesSource(query.value)
        }.flow.cachedIn(viewModelScope)
    }


    private val _langList = mutableStateListOf<String>()
    val langList: List<String>
        get() = _langList

    fun getLanguages(url: String){
        viewModelScope.launch {
            try {
                RetrofitClient.apiService.getLanguages(url).forEach {
                    _langList.add(it.key)
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}