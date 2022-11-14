package com.dirzaaulia.avowstmdb.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dirzaaulia.avowstmdb.R
import com.dirzaaulia.avowstmdb.data.ui_state.ItemTrailerState
import com.dirzaaulia.avowstmdb.databinding.FragmentTrailerBinding
import com.dirzaaulia.avowstmdb.screens.detail.adapter.TrailerAdapter
import com.dirzaaulia.avowstmdb.util.*
import kotlinx.coroutines.launch

class TrailerFragment: Fragment() {

    private lateinit var binding: FragmentTrailerBinding

    private val viewModel: DetailViewModel by hiltNavGraphViewModels(R.id.details_nav_graph)

    private val adapter = TrailerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrailerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovieTrailers()
        setupAdapter()
        subscribeTrailers()
    }

    private fun subscribeTrailers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trailers.collect { state ->
                    when {
                        state.isLoading -> {
                            binding.root.displayedChild = 0
                            adapter.submitList(ItemTrailerState.getListItemTrailerStatePlaceholder())
                        }
                        state.isSucceeded -> {
                            state.success { listData ->
                                if (listData.isEmpty()) {
                                   setupErrorView("There Is No Trailer")
                                } else {
                                    binding.root.displayedChild = 0
                                    adapter.submitList(listData)
                                }
                            }
                        }
                        state.isError -> {
                            state.error {
                                setupErrorView(it.message.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun setupErrorView(errorMessage: String) {
        binding.apply {
            root.displayedChild = 1
            viewCommonError.apply {
                this.errorMessage.text = errorMessage
                buttonRetry.setOnClickListener {
                    viewModel.getMovieTrailers()
                }
            }
        }
    }


}