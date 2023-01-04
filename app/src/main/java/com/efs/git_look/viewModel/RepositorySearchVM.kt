package com.efs.git_look.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
    var isRepoLoading = mutableStateOf(false)
    var repositories: Flow<PagingData<Repository>> = Pager(PagingConfig(pageSize = 10)){
        RepositoriesSource(query.value)
    }.flow.cachedIn(viewModelScope)

    fun refresh()= viewModelScope.launch{
        _isRefreshing.update { true }
        delay(2000)
        _isRefreshing.update { false }
        repositories = Pager(PagingConfig(pageSize = 20)){
            RepositoriesSource(query.value)
        }.flow.cachedIn(viewModelScope)
    }

    var repository by mutableStateOf<Repository?>(null)
        private set
    var errorMessage: String by mutableStateOf("")

    fun getRepository (url: String){
        viewModelScope.launch {
            isRepoLoading.value = true

            try {
                val repo = RetrofitClient.apiService.getRepository(url).also {
                    it.languages = RetrofitClient.apiService.getLanguages(it.languages_url)
                    it.releases = RetrofitClient.apiService.getReleases("$url/releases")
                }.apply {
                    isRepoLoading.value = false
                }

                repository = repo
            }catch (e: Exception){
                e.localizedMessage?.let { errorMessage=it }
                isRepoLoading.value = false
            }
        }
    }
}