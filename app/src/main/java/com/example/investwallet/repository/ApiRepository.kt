package com.example.investwallet.repository

import android.util.Log
import com.example.investwallet.api.JSONHeadlinesAPI
import com.example.investwallet.api.JSONSearchApi
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.converter.IUTag
import com.example.investwallet.dto.converter.newsDtoItem
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

    val symbol: MutableStateFlow<IUTag?> = MutableStateFlow(null)
    val detailNews: MutableStateFlow<newsDtoItem?> = MutableStateFlow(null)

    suspend fun getListTicket(text: String, exchange: String = "", type: String = "stock"): List<QuoteDTO>{
        return apiJSONSearch.getFindQuotes(text, "ru",type, exchange)
    }

    suspend fun getHeadlines(
        tag: String,
        category: String = "stock",
        lang: String = "ru"): List<newsDtoItem>{
        Log.e("tag",tag ?: "почему")
        return try {
            api.getHeadlines(category, lang, tag)
        }catch (e: Exception){
            Log.e("tag",e.message.toString())

            emptyList<newsDtoItem>()
        }

    }



}