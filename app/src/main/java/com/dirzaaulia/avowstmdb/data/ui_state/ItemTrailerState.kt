package com.dirzaaulia.avowstmdb.data.ui_state

import com.dirzaaulia.avowstmdb.data.model.Video

data class ItemTrailerState(
    val trailer: Video,
    val isPlaceholder: Boolean = false
) {
    companion object {
        fun getListItemTrailerStatePlaceholder() : List<ItemTrailerState> {
            val trailerState = mutableListOf<ItemTrailerState>()
            for (i in 1..20) {
                val genreState = ItemTrailerState(
                    trailer = Video(),
                    isPlaceholder = true
                )
                trailerState.add(genreState)
            }
            return trailerState.toList()
        }
    }
}