package com.dirzaaulia.avowstmdb.screens.detail

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dirzaaulia.avowstmdb.data.model.Review
import com.dirzaaulia.avowstmdb.data.ui_state.ItemTrailerState
import com.dirzaaulia.avowstmdb.paging.ReviewsPagingSource
import com.dirzaaulia.avowstmdb.repository.Repository
import com.dirzaaulia.avowstmdb.util.Constant
import com.dirzaaulia.avowstmdb.util.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository,
): ViewModel() {

    /**
     * Response Result
     */
    private val _trailers: MutableStateFlow<ResponseResult<List<ItemTrailerState>>> =
        MutableStateFlow(ResponseResult.Success(emptyList()))
    val trailers = _trailers.asStateFlow()

    /**
     * State Flow
     */
    private val _movieId: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Paging Data
     */
    val reviews: Flow<PagingData<Review>> = _movieId.flatMapLatest { movieId ->
        Pager(
            config = PagingConfig(pageSize = Constant.TMDB_PAGE_SIZE),
            pagingSourceFactory = {
                ReviewsPagingSource(
                    repository = repository,
                    movieId = movieId
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    @MainThread
    fun setMovieId(movieId: String) {
        _movieId.value = movieId
    }

    fun getMovieTrailers() {
        if (_movieId.value.isNotBlank()) {
            repository.getMovieVideos(_movieId.value)
                .onEach { _trailers.value = it }
                .launchIn(viewModelScope)
        }
    }
}