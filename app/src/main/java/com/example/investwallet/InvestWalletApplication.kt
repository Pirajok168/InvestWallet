package com.example.investwallet

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.example.investwallet.dto.QuoteDTO
import dagger.hilt.android.HiltAndroidApp




@HiltAndroidApp
class InvestWalletApplication: Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            // Disable `Cache-Control` header support as some podcast images disable disk caching.
            .respectCacheHeaders(false)
            .build()
    }

}