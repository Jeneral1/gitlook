package com.efs.git_look.model

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.efs.git_look.network.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class RepositoriesSource (
    private val searchQuery: String,
): PagingSource<Int, Repository>() {

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return try {
            val perPage = 10
            val page = params.key?: 1
            val repositoryResponse = RetrofitClient.apiService.searchRepositoryList(page = page, per_page = perPage, q=searchQuery)

            val repoList = mutableListOf<Repository>()
            repositoryResponse.items.forEach { it ->
                repoList.add(RetrofitClient.apiService.getRepository(it.url).also {
                    it.languages = RetrofitClient.apiService.getLanguages(it.languages_url)
                })
            }
            LoadResult.Page(
                data = repoList,
                prevKey = if(page==1) null else page-1,
                nextKey = if(repositoryResponse.items.isEmpty()) null else page +1
            )
        }catch (e: IOException){
            return LoadResult.Error(e)
        }catch (e: HttpException){
            return LoadResult.Error(e)
        }
    }
}