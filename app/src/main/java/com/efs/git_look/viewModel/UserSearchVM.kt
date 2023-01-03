package com.efs.git_look.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.efs.git_look.model.User
import com.efs.git_look.model.UsersSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserSearchVM: ViewModel() {

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
    var user: Flow<PagingData<User>> = Pager(PagingConfig(pageSize = 10)){
        UsersSource(query.value)
    }.flow.cachedIn(viewModelScope)

    fun refresh()= viewModelScope.launch{
        _isRefreshing.update { true }
        delay(2000)
        _isRefreshing.update { false }
        user = Pager(PagingConfig(pageSize = 20)){
            UsersSource(query.value)
        }.flow.cachedIn(viewModelScope)
    }
}