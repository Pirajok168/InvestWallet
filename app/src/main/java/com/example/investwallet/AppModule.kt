package com.example.investwallet

import com.example.investwallet.api.JSONSearchApi
import com.example.investwallet.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

private const val URL = "https://symbol-search.tradingview.com"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("apiJSONSearch")
    fun provideJSONSearchApi(): JSONSearchApi = Retrofit.Builder()
        .baseUrl(URL)
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(JSONSearchApi::class.java)


    @Singleton
    @Provides
    fun provideApiRepository(
        @Named("apiJSONSearch") apiJSONSearch: JSONSearchApi
    ): ApiRepository = ApiRepository(apiJSONSearch)

}