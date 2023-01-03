package com.efs.git_look.model

import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.efs.git_look.network.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

/**
 * Users source
 *
 * @property searchQuery
 * @constructor Create empty Users source
 */
class UsersSource(
    private val searchQuery: String,
): PagingSource<Int, User>() {
    var totalResult = mutableStateOf(0)
    var showing = mutableStateOf(0)
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val perPage = 10
            val page = params.key?: 1
            val userResponse = RetrofitClient.apiService.searchUserList(page = page, per_page = perPage, q=searchQuery)
            totalResult.value = userResponse.total_count
            showing.value = perPage * page

            val userList = mutableListOf<User>()
            userResponse.items.forEach {
                userList.add(RetrofitClient.apiService.getUSer(it.url))
            }
            LoadResult.Page(
                data = userList,
                prevKey = if(page==1) null else page-1,
                nextKey = if(userResponse.items.isEmpty()) null else page +1
            )
        }catch (e: IOException){
            return LoadResult.Error(e)
        }catch (e: HttpException){
            return LoadResult.Error(e)
        }
    }

}