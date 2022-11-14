package com.dirzaaulia.avowstmdb.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.avowstmdb.data.response.DiscoverMovieResponse
import com.dirzaaulia.avowstmdb.data.response.GenresResponse
import com.dirzaaulia.avowstmdb.data.response.ReviewsResponse
import com.dirzaaulia.avowstmdb.data.response.TrailerResponse
import com.dirzaaulia.avowstmdb.network.Service
import com.dirzaaulia.avowstmdb.util.ResponseResult
import com.dirzaaulia.avowstmdb.util.executeWithResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

class Repository @Inject constructor(
    private val service: Service
) {

    @WorkerThread
    fun getGenres() = flow {
        emit(ResponseResult.Loading)
        try {
            val response = service.getGenres()
            response.body()?.let {
                emit(ResponseResult.Success(GenresResponse.toListGenreState(it)))
            } ?: run {
                throw HttpException(response)
            }
        } catch (throwable: Throwable) {
            emit(ResponseResult.Error(throwable))
        }
    }.flowOn(Dispatchers.IO)

    @WorkerThread
    suspend fun discoverMovie(
        page: Int,
        genres: String
    ) = withContext(Dispatchers.IO) {
        executeWithResponse {
            val response = service.discoverMovie(
                page = page,
                genres = genres
            )
            response.body() ?: run { throw HttpException(response) }
        }
    }

    @WorkerThread
    suspend fun getMovieReviews(
        movieId: String,
        page: Int,
    ) = withContext(Dispatchers.IO) {
        executeWithResponse {
            val response = service.getMovieReviews(
                id = movieId,
                page = page
            )
            response.body() ?: run { throw HttpException(response) }
        }
    }

    @WorkerThread
    fun getMovieVideos(movieId: String) = flow {
        emit(ResponseResult.Loading)
        try {
            val response = service.getMovieVideos(movieId = movieId)
            response.body()?.let {
                emit(ResponseResult.Success(TrailerResponse.toListItemTrailerState(it.results)))
            } ?: run {
                throw HttpException(response)
            }
        } catch (throwable: Throwable) {
            emit(ResponseResult.Error(throwable))
        }
    }.flowOn(Dispatchers.IO)
}