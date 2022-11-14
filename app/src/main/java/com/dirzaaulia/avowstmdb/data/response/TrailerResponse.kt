package com.dirzaaulia.avowstmdb.data.response

import com.dirzaaulia.avowstmdb.data.model.Video
import com.dirzaaulia.avowstmdb.data.ui_state.ItemTrailerState

data class TrailerResponse(
    val id: Int?,
    val results: List<Video> = emptyList()
) {
    companion object {
        private fun toItemTrailerState(video: Video): ItemTrailerState {
            return ItemTrailerState(trailer = video)
        }

        fun toListItemTrailerState(list: List<Video>): List<ItemTrailerState> {
            return list
                .filter {
                    it.type?.contains("Trailer", true) == true &&
                            it.site?.contains("Youtube", true) == true
                }
                .map { toItemTrailerState(it) }
        }
    }
}