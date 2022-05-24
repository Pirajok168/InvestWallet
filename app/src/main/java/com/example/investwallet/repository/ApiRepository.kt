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
import javax.inject.Inject


class ApiRepository @Inject constructor(
    private val apiJSONSearch: JSONSearchApi
) {

    suspend fun getListTicket(text: String): List<QuoteDTO>{
        return apiJSONSearch.getFindQuotes(text, "ru","stock")
    }




}