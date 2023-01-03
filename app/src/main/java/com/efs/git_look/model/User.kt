package com.efs.git_look.model

import com.squareup.moshi.Json

/**
 * User
 *
 * @property login
 * @property id
 * @property avatar_url
 * @property url
 * @property repos_url
 * @property score
 * @property name
 * @property location
 * @property bio
 * @property public_repos
 * @property followers
 * @property updated_at
 * @constructor Create empty User
 */
data class User(
    @field: Json(name = "login")
    val login: String,
    @field: Json(name = "id")
    val id: Int,
    @field: Json(name = "avatar_url")
    val avatar_url: String,
    @field: Json(name = "url")
    val url: String,
    @field: Json(name = "repos_url")
    val repos_url: String,
    @field: Json(name = "score")
    val score: String,
    @field: Json(name = "name")
    val name: String? = "",
    @field: Json(name = "location")
    val location: String? = "",
    @field: Json(name = "bio")
    val bio: String? = "",
    @field: Json(name = "public_repos")
    val public_repos: Int? = 0,
    @field: Json(name = "followers")
    val followers: Int? = 0,
    @field: Json(name = "updated_at")
    val updated_at: String? = "",
)

data class UserResponse(
    val items: List<User>,
    val total_count: Int,
    val incomplete_results: Boolean = false
)
