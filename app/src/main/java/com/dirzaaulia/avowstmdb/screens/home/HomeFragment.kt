package com.dirzaaulia.avowstmdb.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.paging.LoadState
import com.dirzaaulia.avowstmdb.data.model.Movie
import com.dirzaaulia.avowstmdb.data.ui_state.ItemGenreState
import com.dirzaaulia.avowstmdb.databinding.FragmentHomeBinding
import com.dirzaaulia.avowstmdb.util.CommonLoadStateAdapter
import com.dirzaaulia.avowstmdb.screens.home.adapter.DiscoverAdapter
import com.dirzaaulia.avowstmdb.screens.home.adapter.GenreAdapter
import com.dirzaaulia.avowstmdb.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val genreAdapter = GenreAdapter(this::onGenreClicked)
    private val discoverAdapter = DiscoverAdapter(this::onDiscoverItemClicked)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        subscribeGenres()
        subscribeDiscover()
    }

    private fun subscribeGenres() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genres.collect { state ->
                    setUIBasedOnGenresState(state)
                    when {
                        state.isLoading -> { genreAdapter.submitList(ItemGenreState.getClipStatesPlaceholder()) }
                        state.isSucceeded -> {
                            state.success {
                                genreAdapter.submitList(it)
                            }
                        }
                        state.isError -> {
                            state.error {
                                val errorMessage = it.message
                                binding.viewGenresError.apply {
                                    this.errorMessage.text = errorMessage
                                    buttonRetry.setOnClickListener { viewModel.getGenres() }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun subscribeDiscover() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.discoverMovie.collectLatest {
                    discoverAdapter.submitData(it)
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.genreRecyclerView.adapter = genreAdapter
        setupDiscoverAdapter()
    }

    private fun setupDiscoverAdapter() {
        binding.discoverRecyclerView.adapter = discoverAdapter.withLoadStateHeaderAndFooter(
            CommonLoadStateAdapter { discoverAdapter.retry() },
            CommonLoadStateAdapter { discoverAdapter.retry() }
        )

        discoverAdapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> binding.discoverViewAnimator.displayedChild = 0
                is LoadState.NotLoading -> {
                    if (discoverAdapter.itemCount == 0)
                        setDiscoverEmptyView()
                    else
                        binding.discoverViewAnimator.displayedChild = 1
                }
                is LoadState.Error -> {
                    val errorMessage = (loadState.source.refresh as LoadState.Error).error.message
                    setDiscoverErrorView(errorMessage.toString()) {
                        discoverAdapter.retry()
                    }
                }
            }
        }
    }

    private fun setUIBasedOnGenresState(state: ResponseResult<List<ItemGenreState>>) {
        binding.apply {
            genreRecyclerView.isVisible = !state.isError
            viewGenresError.root.isVisible = state.isError
        }
    }

    private fun setDiscoverEmptyView() {
        binding.apply {
            discoverViewAnimator.displayedChild = 2
            viewDiscoverError.apply {
                errorMessage.text = "There Is No Movie To Discover!"
                buttonRetry.isVisible = false
            }
        }
    }

    private fun setDiscoverErrorView(
        errorMessage: String,
        retry: () -> Unit
    ) {
        binding.apply {
            discoverViewAnimator.displayedChild = 2
            viewDiscoverError.apply {
                this.errorMessage.text = errorMessage
                buttonRetry.setOnClickListener { retry.invoke() }
            }
        }
    }

    private fun onGenreClicked(item: ItemGenreState, position: Int) {
        viewModel.apply {
            genreChecked = position
            if (item.isChecked) {
                removeGenre(item.genre.id.toString())
            } else {
                setGenres(item.genre.id.toString())
            }
        }
        genreAdapter.updateItem(
            item = item,
            position = position
        )
    }

    private fun onDiscoverItemClicked(item: Movie) {
        val directions = HomeFragmentDirections.actionGenresFragmentToDetailFragment(
            movieId = item.id.toString(),
            name = item.originalTitle.toString(),
            poster = item.posterPath.toString(),
            overview = item.overview.toString()
        )
        binding.root.findNavController().navigate(directions)
    }
}