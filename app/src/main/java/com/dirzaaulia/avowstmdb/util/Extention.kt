package com.dirzaaulia.avowstmdb.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.dirzaaulia.avowstmdb.R

fun ImageView.loadTMDBPoster(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(this.context, R.color.md_theme_dark_onSurfaceVariant)
    )
    circularProgressDrawable.start()

    this.load("${Constant.TMDB_POSTER_URL}$url") {
        crossfade(true)
        placeholder(circularProgressDrawable)
    }
}

fun ImageView.loadNetworkImage(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(this.context, R.color.md_theme_dark_onSurfaceVariant)
    )
    circularProgressDrawable.start()

    this.load(url) {
        crossfade(true)
        placeholder(circularProgressDrawable)
    }
}