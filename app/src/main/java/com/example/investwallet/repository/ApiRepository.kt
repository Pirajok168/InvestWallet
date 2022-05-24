package com.example.investwallet.repository

import android.util.Log
import com.example.investwallet.api.JSONSearchApi
import com.example.investwallet.dto.QuoteDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


private const val URL = "https://symbol-search.tradingview.com"
class ApiRepository {
    private var retrofit: Retrofit? = null
    private var jsonSearchApi: JSONSearchApi? = null


    init {
        retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        jsonSearchApi = retrofit?.create(JSONSearchApi::class.java)

    }


    companion object{
        private var apiRepository: ApiRepository? = null
        operator fun invoke(): ApiRepository? {
            return if (apiRepository == null){
                ApiRepository()
            }else apiRepository
        }
    }


}