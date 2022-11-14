package com.dirzaaulia.avowstmdb.screens.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dirzaaulia.avowstmdb.data.ui_state.ItemGenreState
import com.dirzaaulia.avowstmdb.databinding.ItemGenreBinding
import com.dirzaaulia.avowstmdb.databinding.ShimmerItemGenreBinding

typealias OnGenreItemClicked = (ItemGenreState, Int) -> Unit

class GenreAdapter(
    private val onGenreItemClicked: OnGenreItemClicked
): ListAdapter<ItemGenreState, GenreAdapter.ViewHolder>(COMPARATOR) {

    override fun getItemViewType(position: Int): Int {
        val isPlaceholder = currentList[position].isPlaceholder
        return if(isPlaceholder) PLACEHOLDER else DATA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            PLACEHOLDER -> {
                ViewHolder.PlaceholderViewHolder(
                    ShimmerItemGenreBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            DATA -> {
                ViewHolder.ItemViewHolder(
                    ItemGenreBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    onGenreItemClicked
                )
            }
            else -> throw IllegalArgumentException("Invalid ViewType Provided")
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

    sealed class ViewHolder(
        binding: ViewBinding
    ): RecyclerView.ViewHolder(binding.root) {

        class PlaceholderViewHolder(
            binding: ShimmerItemGenreBinding
        ) : ViewHolder(binding)

        class ItemViewHolder(
            private val binding: ItemGenreBinding,
            private val onGenreItemClicked: OnGenreItemClicked
        ): ViewHolder(binding) {

            fun bind(item: ItemGenreState) {
                binding.root.apply {
                    isChecked = item.isChecked
                    text = item.genre.name
                    setOnClickListener {
                        onGenreItemClicked(item, bindingAdapterPosition)
                    }
                }
            }
        }
    }

    fun updateItem(
        item: ItemGenreState,
        position: Int
    ) {
        currentList[position].isChecked = !item.isChecked
        notifyItemChanged(position)
    }

    companion object {
        const val PLACEHOLDER = 0
        const val DATA = 1

        val COMPARATOR = object: DiffUtil.ItemCallback<ItemGenreState>() {
            override fun areItemsTheSame(oldItem: ItemGenreState, newItem: ItemGenreState): Boolean {
                return oldItem.genre.id == newItem.genre.id
            }

            override fun areContentsTheSame(oldItem: ItemGenreState, newItem: ItemGenreState): Boolean {
               return oldItem == newItem
            }
        }
    }
}