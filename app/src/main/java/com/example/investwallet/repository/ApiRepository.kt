package com.example.investwallet.repository

import android.util.Log
import com.example.investwallet.api.JSONHeadlinesAPI
import com.example.investwallet.api.JSONSearchApi
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.headlines.Headline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Query
import javax.inject.Inject


class ApiRepository @Inject constructor(
    private val apiJSONSearch: JSONSearchApi,
    private val api: JSONHeadlinesAPI
) {

    val symbol: MutableStateFlow<QuoteDTO?> = MutableStateFlow(null)

    suspend fun getListTicket(text: String): List<QuoteDTO>{
        return apiJSONSearch.getFindQuotes(text, "ru","stock")
    }

    suspend fun getHeadlines(
        category: String = "stock",
        lang: String = "ru"): List<Headline>{
        Log.e("tag",symbol.value?.tagHttp ?: "почему")
        return try {
            api.getHeadlines(category, lang, symbol.value?.tagHttp ?: " ")
        }catch (e: Exception){
            Log.e("tag",e.message.toString())

            emptyList<Headline>()
        }

    }



}