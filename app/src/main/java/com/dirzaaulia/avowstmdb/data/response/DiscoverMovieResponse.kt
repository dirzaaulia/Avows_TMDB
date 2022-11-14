package com.dirzaaulia.avowstmdb.data.response

import com.dirzaaulia.avowstmdb.data.model.Movie
import com.squareup.moshi.Json

data class DiscoverMovieResponse(
    @Json(name = "page")
    val page: Int = 0,
    @Json(name = "results")
    val results: List<Movie> = emptyList(),
    @Json(name = "total_pages")
    val totalPages: Int = 0,
    @Json(name = "total_results")
    val totalResults: Int = 0,
)