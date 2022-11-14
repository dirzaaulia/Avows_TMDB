package com.dirzaaulia.avowstmdb.screens.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dirzaaulia.avowstmdb.data.model.Review
import com.dirzaaulia.avowstmdb.databinding.ItemReviewBinding
import com.dirzaaulia.avowstmdb.util.loadNetworkImage

class ReviewsAdapter: PagingDataAdapter<Review, ReviewsAdapter.ViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(item) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReviewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    inner class ViewHolder(
        private val binding: ItemReviewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            binding.apply {
                val avatarUrl =
                    item.authorDetails?.avatarPath?.replaceFirst("/", "")
                avatar.loadNetworkImage(avatarUrl.toString())
                username.text = item.authorDetails?.username
                rating.text = "${item.authorDetails?.rating ?: 0}"
                review.text = item.content
            }
        }
    }

    companion object {
        val COMPARATOR = object: DiffUtil.ItemCallback<Review>() {
            override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
                return oldItem == newItem
            }
        }
    }
}