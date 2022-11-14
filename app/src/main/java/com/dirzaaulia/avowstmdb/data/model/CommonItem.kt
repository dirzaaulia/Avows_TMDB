package com.dirzaaulia.avowstmdb.data.model

import com.squareup.moshi.Json

data class CommonItem(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "name")
    val name: String? = null,
    @Json(name = "logo_path")
    val logoPath: String? = null,
)