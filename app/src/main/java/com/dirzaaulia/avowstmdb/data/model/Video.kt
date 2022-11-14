package com.dirzaaulia.avowstmdb.data.model

import com.squareup.moshi.Json

data class Video(
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "key")
    val key: String? = null,
    @Json(name = "site")
    val site: String? = null,
    @Json(name = "size")
    val size: Int? = null,
    @Json(name = "type")
    val type: String? = null,
    @Json(name = "official")
    val official: Boolean? = null,
    @Json(name = "id")
    val id: String? = null,
    @Json(name = "published_at")
    val publishedAt: String? = null
)