package com.dirzaaulia.avowstmdb.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.dirzaaulia.avowstmdb.data.response.DiscoverMovieResponse
import com.dirzaaulia.avowstmdb.util.Constant
import com.dirzaaulia.avowstmdb.data.response.GenresResponse
import com.dirzaaulia.avowstmdb.data.response.ReviewsResponse
import com.dirzaaulia.avowstmdb.data.response.TrailerResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Service {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = Constant.TMDB_KEY
    ): Response<GenresResponse>

    @GET("discover/movie")
    suspend fun discoverMovie(
        @Query("api_key") apiKey: String = Constant.TMDB_KEY,
        @Query("page") page: Int,
        @Query("with_genres") genres: String = ""
    ): Response<DiscoverMovieResponse>

    @GET("movie/{movieId}/reviews")
    suspend fun getMovieReviews(
        @Path("movieId") id: String,
        @Query("api_key") apiKey: String = Constant.TMDB_KEY,
        @Query("page") page: Int
    ): Response<ReviewsResponse>

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = Constant.TMDB_KEY
    ): Response<TrailerResponse>

    companion object {
        fun create(context: Context): Service {
            val httpInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val chuckerInterceptor = ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()

            val client = OkHttpClient.Builder()
                .addInterceptor(httpInterceptor)
                .addInterceptor(chuckerInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(Constant.TMDB_BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(Service::class.java)
        }
    }
}