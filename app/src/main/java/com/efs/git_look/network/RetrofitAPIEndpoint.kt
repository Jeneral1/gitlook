package com.efs.git_look.network

import com.efs.git_look.BuildConfig
import com.efs.git_look.model.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

const val BASE_URL = "https://api.github.com/"
/**
 * Network API endpoints interface
 * */
interface RetrofitAPIEndpoint{
    /**
     * Https get request for a list of [User].
     *
     * @param[page] an annotated query of type: Int
     * @param[per_page] an annotated query of type: Int
     * @param[q] an annotated query of type: String
     * @return List of [User]s
     *
     * */
    @GET("search/users")
    suspend fun searchUserList(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("q") q:String,
    ) : UserResponse

    /**
     * Https get request for a list of [Repository].
     *
     * @param[page] an annotated query of type: Int
     * @param[per_page] an annotated query of type: Int
     * @param[q] an annotated query of type: String
     * @return List of [Repository]
     *
     * */
    @GET("search/repositories")
    suspend fun searchRepositoryList(
        @Query("page") page: Int,
        @Query("per_page") per_page: Int,
        @Query("q") q:String,
    ) : RepositoryResponse



    /**
     * Https get request to get a User
     *
     * @param[url] annotated as Url and type: String. This Url will override the BASE_URL
     * @return a [User] object
     *
     * */
    @GET
    suspend fun getUSer(@Url url: String) : User

    /**
     * Https get request to get a Repository
     *
     * @param[url] annotated as Url and type: String. This Url will override the BASE_URL
     * @return a [Repository] object
     *
     * */
    @GET
    suspend fun getRepository(@Url url: String) : Repository


    @GET
    suspend fun getLanguages(@Url url: String) : Map<String, Int>


    @GET
    suspend fun getReleases(@Url url: String) : List<Release>


    @GET
    suspend fun getUserRepositories(@Url url: String) : List<Repository>


}

/**
 * Public object which can be accessed from anywhere within this project
 * */
object RetrofitClient{
    /**
     * A retrofit client function
     *
     * @return instance of the [Retrofit] class which creates an instance using Builder pattern
     * */
    private fun getClient(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient.build())
            .build()
    }


    /**
     * Ok http client
     */
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor.invoke {
        val original = it.request()
        val req: Request = original.newBuilder()
            .header("Accept", "application/vnd.github+json")
            .header("Authorization", "Bearer ${BuildConfig.API_TOKEN}")
            .header("X-GitHub-Api-Version", "2022-11-28")
            .build()
        return@invoke it.proceed(req)
    })

    /**
     * In implementation of the Https network API endpoints {@link RetrofitAPIEndpoint [RetrofitAPIEndpoint]}
     * */
    val apiService: RetrofitAPIEndpoint =
        getClient().create(RetrofitAPIEndpoint::class.java)
}