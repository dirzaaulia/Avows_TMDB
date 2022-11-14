package com.dirzaaulia.avowstmdb.data.model

import com.squareup.moshi.Json

data class Movie(
    @Json(name = "backdrop_path")
    val backdropPath: String? = null,
    @Json(name = "genre_ids")
    val genreId: List<Int> = emptyList(),
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "original_title")
    val originalTitle: String? = null,
    @Json(name = "overview")
    val overview: String? = null,
    @Json(name = "poster_path")
    val posterPath: String? = null,
    @Json(name = "release_date")
    val releaseDate: String? = null,
    @Json(name = "title")
    val title: String? = null,
    @Json(name = "vote_average")
    val voteAverage: Double? = null,
    @Json(name = "vote_count")
    val voteCount: Int? = null,
    @Json(name = "budget")
    val budget: Long? = null,
    @Json(name = "genres")
    val genres: List<CommonItem> = emptyList(),
    @Json(name = "homepage")
    val homepage: String? = null,
    @Json(name = "production_companies")
    val productionCompanies: List<CommonItem> = emptyList(),
    @Json(name = "revenue")
    val revenue: Long? = null,
    @Json(name = "runtime")
    val runtime: Int? = null,
    @Json(name = "status")
    val status: String? = null,
    @Json(name = "tagline")
    val tagline: String? = null,
) {

}