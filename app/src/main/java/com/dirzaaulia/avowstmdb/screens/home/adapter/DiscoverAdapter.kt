package com.dirzaaulia.avowstmdb.screens.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.dirzaaulia.avowstmdb.R
import com.dirzaaulia.avowstmdb.data.model.Movie
import com.dirzaaulia.avowstmdb.databinding.ItemDiscoverBinding
import com.dirzaaulia.avowstmdb.util.Constant
import com.dirzaaulia.avowstmdb.util.loadTMDBPoster

typealias OnDiscoverItemClicked = (Movie) -> Unit

class DiscoverAdapter(
    private val onDiscoverItemClicked: OnDiscoverItemClicked
): PagingDataAdapter<Movie, DiscoverAdapter.ViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDiscoverBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class ViewHolder(
        private val binding: ItemDiscoverBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Movie) {
            binding.apply {
                root.setOnClickListener {
                    onDiscoverItemClicked(item)
                }

                val circularProgressDrawable = CircularProgressDrawable(this.root.context)
                circularProgressDrawable.setColorSchemeColors(
                    ContextCompat.getColor(this.root.context, R.color.md_theme_dark_onSurfaceVariant)
                )
                circularProgressDrawable.start()

                poster.loadTMDBPoster("${Constant.TMDB_POSTER_URL}${item.posterPath}")
                name.text = item.originalTitle
            }
        }
    }

    companion object {
        val COMPARATOR = object: DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        }
    }
}