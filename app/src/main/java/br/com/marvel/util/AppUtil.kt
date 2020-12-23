package br.com.marvel.util

import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import br.com.marvel.BuildConfig
import br.com.marvel.R
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.util.concurrent.TimeUnit


object AppUtil {

    fun loadImageWithHeader(context: Context, url: String, image: AppCompatImageView) {
        val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

        val urlWithHeader = GlideUrl(
            url, LazyHeaders.Builder()
                .addHeader(MARVEL_APIKEY, BuildConfig.API_PUBLIC)
                .addHeader(
                    MARVEL_HASH,
                    HashGenerate.generate(
                        timeStamp,
                        BuildConfig.API_PRIVATE,
                        BuildConfig.API_PUBLIC
                    )
                )
                .addHeader(MARVEL_TIMESTAMP, timeStamp.toString())
                .build()
        )

        GlideApp.with(context)
            .load(urlWithHeader)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(image)
    }
}