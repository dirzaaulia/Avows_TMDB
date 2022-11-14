package com.dirzaaulia.avowstmdb.data.ui_state

import com.dirzaaulia.avowstmdb.data.model.CommonItem

data class ItemGenreState(
    val genre: CommonItem,
    var isChecked: Boolean,
    val isPlaceholder: Boolean = false
) {
    companion object {
        fun getClipStatesPlaceholder(): List<ItemGenreState> {
            val genresState = mutableListOf<ItemGenreState>()
            for (i in 1..20) {
                val genreState = ItemGenreState(
                    genre = CommonItem(),
                    isChecked = false,
                    isPlaceholder = true
                )
                genresState.add(genreState)
            }
            return genresState.toList()
        }
    }
}