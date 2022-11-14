package com.dirzaaulia.avowstmdb.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.dirzaaulia.avowstmdb.R
import com.dirzaaulia.avowstmdb.databinding.FragmentReviewsBinding
import com.dirzaaulia.avowstmdb.screens.detail.adapter.ReviewsAdapter
import com.dirzaaulia.avowstmdb.util.CommonLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ReviewsFragment: Fragment() {

    private lateinit var binding: FragmentReviewsBinding

    private val viewModel: DetailViewModel by hiltNavGraphViewModels(R.id.details_nav_graph)

    private val adapter = ReviewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        subscribeReviews()
    }

    private fun subscribeReviews() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviews.collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            CommonLoadStateAdapter { adapter.retry() },
            CommonLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                is LoadState.Loading -> binding.root.displayedChild = 0
                is LoadState.NotLoading -> {
                    if (adapter.itemCount == 0) {
                       setEmptyView()
                    } else {
                        binding.root.displayedChild = 1
                    }
                }
                is LoadState.Error -> {
                    val errorMessage = (loadState.source.refresh as LoadState.Error).error.message
                    setErrorView(errorMessage.toString())
                }
            }
        }
    }

    private fun setEmptyView() {
        binding.apply {
            root.displayedChild = 2
            viewCommonError.apply {
                errorMessage.text = "There Is No Review!"
                buttonRetry.isVisible = false
            }
        }
    }

    private fun setErrorView(errorMessage: String) {
        binding.apply {
            root.displayedChild = 2
            viewCommonError.apply {
                this.errorMessage.text = errorMessage
                buttonRetry.setOnClickListener { adapter.retry() }
            }
        }
    }
}