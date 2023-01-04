package com.efs.git_look.model

import com.squareup.moshi.Json

/**
 * Repository
 *
 * @property name
 * @property full_name
 * @property description
 * @property languages_url
 * @property releases_url
 * @property updated_at
 * @property pushed_at
 * @property language
 * @property visibility
 * @property subscribers_count
 * @property stargazers_count
 * @constructor Create empty Repository
 */
data class Repository(
    @field: Json(name = "name")
    val name: String? = "",
    @field: Json(name = "full_name")
    val full_name: String,
    @field: Json(name = "description")
    val description: String? = "",
    @field: Json(name = "url")
    val url: String,
    @field: Json(name = "languages_url")
    val languages_url: String,
    @field: Json(name = "releases_url")
    val releases_url: String,
    @field: Json(name = "updated_at")
    val updated_at: String? = "",
    @field: Json(name = "pushed_at")
    val pushed_at: String,
    @field: Json(name = "stargazers_count")
    val stargazers_count: Int? = 0,
    @field: Json(name = "language")
    val language: String,
    @field: Json(name = "visibility")
    val visibility: String,
    @field: Json(name = "subscribers_url")
    val subscribers_url: String,
    @field: Json(name = "subscribers_count")
    val subscribers_count: Int? = 0,

    var languages: Map<String, Int>? = mapOf(Pair("",0)),

    var releases: List<Release>? = listOf()
)

data class RepositoryResponse(
    @field: Json(name = "items")
    val items: List<Repository>,
    @field: Json(name = "total_count")
    val total_count: Int,
    @field: Json(name = "incomplete_results")
    val incomplete_results: Boolean = false
)

data class Release(
    @field: Json(name = "url")
    val url: String = "",
    @field: Json(name = "name")
    val name: String,
    @field: Json(name = "published_at")
    val published_at: String,
)
