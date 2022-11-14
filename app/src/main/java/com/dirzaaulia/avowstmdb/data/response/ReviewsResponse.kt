package com.dirzaaulia.avowstmdb.data.response

import com.dirzaaulia.avowstmdb.data.model.Review
import com.squareup.moshi.Json

data class ReviewsResponse(
    @Json(name = "id")
    val id: Int = 0,
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    val results: List<Review> = emptyList(),
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    @Json(name = "total_results")
    val totalResults: Int = 0
)