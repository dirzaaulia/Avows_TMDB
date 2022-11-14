package com.dirzaaulia.avowstmdb.screens.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import com.dirzaaulia.avowstmdb.DetailsNavGraphArgs
import com.dirzaaulia.avowstmdb.R
import com.dirzaaulia.avowstmdb.databinding.FragmentDetailBinding
import com.dirzaaulia.avowstmdb.screens.detail.adapter.DetailsViewPagerAdapter
import com.dirzaaulia.avowstmdb.util.Constant
import com.dirzaaulia.avowstmdb.util.loadTMDBPoster
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private val viewModel: DetailViewModel by hiltNavGraphViewModels(R.id.details_nav_graph)
    private val args: DetailsNavGraphArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCollapsingToolbar()
        setupViewPager()
        setInitialView()
    }

    private fun setupCollapsingToolbar() {
        var isShow = true
        var scrollRange = -1
        binding.appBar.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.collapsingToolbar.title = args.name
                isShow = true
            } else if (isShow) {
                binding.collapsingToolbar.title = " "
                isShow = false
            }
        }
    }

    private fun setupViewPager() {
        val tabs = listOf("Reviews", "Trailer")
        val adapter = DetailsViewPagerAdapter(childFragmentManager, lifecycle)

        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
    }

    private fun setInitialView() {
        binding.apply {
            viewModel.setMovieId(args.movieId)
            viewExpandedToolbar.apply {
                poster.loadTMDBPoster("${Constant.TMDB_POSTER_URL}${args.poster}")
                overview.text = args.overview
            }
            toolbar.title = args.name
        }
    }


}