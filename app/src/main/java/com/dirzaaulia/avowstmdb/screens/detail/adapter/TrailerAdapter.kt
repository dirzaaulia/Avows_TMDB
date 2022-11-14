package com.dirzaaulia.avowstmdb.screens.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dirzaaulia.avowstmdb.data.ui_state.ItemTrailerState
import com.dirzaaulia.avowstmdb.databinding.ItemTrailerBinding
import com.dirzaaulia.avowstmdb.databinding.ShimmerItemTrailerBinding
import com.dirzaaulia.avowstmdb.util.loadNetworkImage

class TrailerAdapter: ListAdapter<ItemTrailerState, TrailerAdapter.ViewHolder>(COMPARATOR) {

    override fun getItemViewType(position: Int): Int {
        val isPlaceholder = currentList[position].isPlaceholder
        return if (isPlaceholder) PLACEHOLDER else DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            PLACEHOLDER -> ViewHolder.PlaceholderViewHolder(
                ShimmerItemTrailerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            DATA ->  ViewHolder.ItemViewHolder(
                ItemTrailerBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw IllegalArgumentException("Incorrect ViewType Provided")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.PlaceholderViewHolder -> { }
            is ViewHolder.ItemViewHolder -> {
                val item = getItem(position)
                item?.let { holder.bind(item) }
            }
        }
    }

    sealed class ViewHolder(binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

        class PlaceholderViewHolder(
            binding: ShimmerItemTrailerBinding
        ): ViewHolder(binding)

        class ItemViewHolder(
            private val binding: ItemTrailerBinding
        ): ViewHolder(binding) {

            fun bind(item: ItemTrailerState) {
                binding.apply {
                    thumbnail.loadNetworkImage("https://img.youtube.com/vi/${item.trailer.key}/hqdefault.jpg")
                    name.text = item.trailer.name
                }
            }
        }
    }

    companion object {
        const val PLACEHOLDER = 0
        const val DATA = 1

        val COMPARATOR = object: DiffUtil.ItemCallback<ItemTrailerState>() {
            override fun areItemsTheSame(
                oldItem: ItemTrailerState,
                newItem: ItemTrailerState
            ): Boolean {
                return oldItem.trailer.id == newItem.trailer.id
            }

            override fun areContentsTheSame(
                oldItem: ItemTrailerState,
                newItem: ItemTrailerState
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}