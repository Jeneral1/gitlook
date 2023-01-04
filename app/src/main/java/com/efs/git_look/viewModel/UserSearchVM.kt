package com.efs.git_look.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.efs.git_look.model.Repository
import com.efs.git_look.model.User
import com.efs.git_look.model.UsersSource
import com.efs.git_look.network.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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
    var users: Flow<PagingData<User>> = Pager(PagingConfig(pageSize = 10)){
        UsersSource(query.value)
    }.flow.cachedIn(viewModelScope)

    fun refresh()= viewModelScope.launch{
        _isRefreshing.update { true }
        delay(2000)
        _isRefreshing.update { false }
        users = Pager(PagingConfig(pageSize = 20)){
            UsersSource(query.value)
        }.flow.cachedIn(viewModelScope)
    }

    var user by mutableStateOf<User?>(null)
        private set
    var isUserLoading =  mutableStateOf(false)
    var userRepoUrl: String by mutableStateOf("")
    var errorMessage: String by mutableStateOf("")

    fun getUser(url: String){
        viewModelScope.launch {
            isUserLoading.value = true
            try {
                user = RetrofitClient.apiService.getUSer(url).apply {
                    userRepoUrl = this.repos_url
                    isUserLoading.value = false
                }
            }catch (e: Exception){
                e.localizedMessage?.let { errorMessage = it }
                isUserLoading.value = false
            }
        }
    }
    //private val _userRepos = mutableStateListOf<Repository>()
    private var getUserRepos: MutableList<Repository> = mutableStateListOf()
    var isRepoLoading = mutableStateOf(false)
    val userRepos : List<Repository>
        get() = getUserRepos

    fun getUserRepos(url: String){
        viewModelScope.launch {
            isRepoLoading.value = true

            try {
                getUserRepos.clear()
                getUserRepos.addAll( RetrofitClient.apiService.getUserRepositories(url).onEach {
                    it.languages = RetrofitClient.apiService.getLanguages(it.languages_url)
                }).apply {
                    userRepoUrl = url
                    isRepoLoading.value = false
                }

            }catch (e: Exception){
                e.printStackTrace()
                e.localizedMessage?.let {
                    errorMessage = it
                }
                isRepoLoading.value = false
            }
        }
    }



}