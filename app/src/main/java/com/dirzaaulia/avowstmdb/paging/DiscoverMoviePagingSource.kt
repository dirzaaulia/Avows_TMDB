package com.dirzaaulia.avowstmdb.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dirzaaulia.avowstmdb.data.model.Movie
import com.dirzaaulia.avowstmdb.repository.Repository
import com.dirzaaulia.avowstmdb.util.Constant
import com.dirzaaulia.avowstmdb.util.pagingSucceeded
import javax.inject.Inject

class DiscoverMoviePagingSource @Inject constructor(
    private val repository: Repository,
    private val genres: String
): PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: Constant.TMDB_STARTING_PAGE_INDEX

        return repository.discoverMovie(
            page = page,
            genres = genres
        ).pagingSucceeded { response ->
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == Constant.TMDB_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page < response.totalPages) page + 1 else null
            )
        }
    }
}