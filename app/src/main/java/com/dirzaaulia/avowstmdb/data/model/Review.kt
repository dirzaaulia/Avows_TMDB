package com.dirzaaulia.avowstmdb.data.model

import com.squareup.moshi.Json

data class Review(
    @Json(name = "author_details")
    val authorDetails: Author?,
    @Json(name = "content")
    val content: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "created_at")
    val createdAt: String?,
    @Json(name = "updated_at")
    val updatedAt: String?,
)