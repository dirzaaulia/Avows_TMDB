package com.dirzaaulia.avowstmdb.screens.home

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dirzaaulia.avowstmdb.data.model.Movie
import com.dirzaaulia.avowstmdb.data.ui_state.ItemGenreState
import com.dirzaaulia.avowstmdb.paging.DiscoverMoviePagingSource
import com.dirzaaulia.avowstmdb.repository.Repository
import com.dirzaaulia.avowstmdb.util.Constant
import com.dirzaaulia.avowstmdb.util.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    /**
     * Response Result
     */
    private val _genres: MutableStateFlow<ResponseResult<List<ItemGenreState>>> =
        MutableStateFlow(ResponseResult.Success(emptyList()))
    val genres = _genres.asStateFlow()

    /**
     * State Flow
     */
    private val _selectedGenres: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Paging Data
     */
    val discoverMovie: Flow<PagingData<Movie>> = _selectedGenres.flatMapLatest { genres ->
        Pager(
            config = PagingConfig(pageSize = Constant.TMDB_PAGE_SIZE),
            pagingSourceFactory = {
                DiscoverMoviePagingSource(
                    repository = repository,
                    genres = genres
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    /**
     * Variable
     */
    var genreChecked: Int = -1

    @MainThread
    fun setGenres(genre: String) {
        _selectedGenres.value += "$genre,"
    }

    @MainThread
    fun removeGenre(genre: String) {
        _selectedGenres.value = _selectedGenres.value.replace("$genre,", "")
    }

    init {
        getGenres()
    }

    fun getGenres() {
        repository.getGenres()
            .onEach { _genres.value = it }
            .launchIn(viewModelScope)
    }
}