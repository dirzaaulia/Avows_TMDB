package com.dirzaaulia.avowstmdb.data.model

import com.squareup.moshi.Json

data class Author(
    @Json(name = "name")
    val name: String?,
    @Json(name = "username")
    val username: String?,
    @Json(name = "avatar_path")
    val avatarPath: String?,
    @Json(name = "rating")
    val rating: Double?
)