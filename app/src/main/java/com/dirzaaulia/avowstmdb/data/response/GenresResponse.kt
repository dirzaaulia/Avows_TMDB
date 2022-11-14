package com.dirzaaulia.avowstmdb.data.response

import com.dirzaaulia.avowstmdb.data.model.CommonItem
import com.dirzaaulia.avowstmdb.data.ui_state.ItemGenreState
import com.squareup.moshi.Json

data class GenresResponse(
    @Json(name = "genres")
    val genres: List<CommonItem> = emptyList()
) {
    companion object {
        private fun toGenreState(data: CommonItem): ItemGenreState {
            return ItemGenreState(
                genre = data,
                isChecked = false
            )
        }

        fun toListGenreState(response: GenresResponse): List<ItemGenreState> {
           return response.genres.map {
               toGenreState(it)
           }
        }
    }
}