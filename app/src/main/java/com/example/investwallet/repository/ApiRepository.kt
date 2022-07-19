package com.example.investwallet.repository

import android.util.Log
import com.example.investwallet.api.JSONHeadlinesAPI
import com.example.investwallet.api.JSONSearchApi
import com.example.investwallet.api.PostJSONApi
import com.example.investwallet.database.ActiveUser
import com.example.investwallet.database.FavoriteTicket
import com.example.investwallet.database.User
import com.example.investwallet.database.UserDatabase
import com.example.investwallet.dto.QuoteDTO
import com.example.investwallet.dto.answer.AnswerDTO
import com.example.investwallet.dto.converter.IUTag
import com.example.investwallet.dto.converter.newsDtoItem
import com.example.investwallet.dto.post.PostDTO
import com.example.investwallet.dto.post.Query
import com.example.investwallet.dto.post.Symbols

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject


sealed class StateCollectData(){
    data class RussiaStock(val answerDTO: AnswerDTO, val symbol: String): StateCollectData()
    data class AmericaStock(val answerDTO: AnswerDTO, val symbol: String): StateCollectData()
    data class CryptoStock(val answerDTO: AnswerDTO, val symbol: String): StateCollectData()
    data class IndiaStock(val answerDTO: AnswerDTO, val symbol: String): StateCollectData()
    data class UkStock(val answerDTO: AnswerDTO, val symbol: String): StateCollectData()
    data class Error(val message: String): StateCollectData()
}

class ApiRepository @Inject constructor(
    private val apiJSONSearch: JSONSearchApi,
    private val api: JSONHeadlinesAPI,
    private val postJSONApi: PostJSONApi
) {

    suspend fun postApi(
        dto: PostDTO
    ){
        postJSONApi.collectDataForShareAmerica(dto)
    }

    val symbol: MutableStateFlow<IUTag?> = MutableStateFlow(null)
    val detailNews: MutableStateFlow<newsDtoItem?> = MutableStateFlow(null)



    suspend fun getListTicket(text: String, exchange: String = "", type: String = "stock"): List<QuoteDTO>{
        return apiJSONSearch.getFindQuotes(text, "ru",type, exchange)
    }

    suspend fun collectDataForShareAmerica(ticket:String): StateCollectData{
        return try {
            val post = postJSONApi.collectDataForShareAmerica(
                PostDTO(
                    columns=listOf("close"),
                    symbols= Symbols(
                        tickers= listOf(ticket),
                        query = Query(
                            types= listOf()
                        )
                    )
                )
            )
            Log.e("error", "${post}")

            StateCollectData.AmericaStock(post, "$")
        }catch (e: java.lang.Exception){
            e.printStackTrace()
            StateCollectData.Error("Неизвестная ошибка")
        }
    }

    suspend fun collectDataForShareRussia(ticket:String): StateCollectData{
        return try {
            val post =postJSONApi.collectDataForShareRussia(
                PostDTO(
                    columns=listOf("close"),
                    symbols= Symbols(
                        tickers= listOf(ticket),
                        query = Query(
                            types= listOf()
                        )
                    )
                )
            )
            StateCollectData.RussiaStock(post, "₽")
        }catch (e: java.lang.Exception){
            e.printStackTrace()
            StateCollectData.Error("Неизвестная ошибка")
        }
    }


    suspend fun collectDataForShareUk(ticket:String): StateCollectData{
        return try {
            val post =postJSONApi.collectDataForShareUK(
                PostDTO(
                    columns=listOf("close"),
                    symbols= Symbols(
                        tickers= listOf(ticket),
                        query = Query(
                            types= listOf()
                        )
                    )
                )
            )
            val state = if (post.totalCount == 0){
                StateCollectData.Error("Ошибка")
            }else{
                StateCollectData.UkStock(post, "£")
            }
            state

        }catch (e: java.lang.Exception){
            e.printStackTrace()
            StateCollectData.Error("Неизвестная ошибка")
        }
    }

    suspend fun collectDataForShareIndia(ticket:String): StateCollectData{
        return try {
            val post =postJSONApi.collectDataForShareIndia(
                PostDTO(
                    columns=listOf("close"),
                    symbols= Symbols(
                        tickers= listOf(ticket),
                        query = Query(
                            types= listOf()
                        )
                    )
                )
            )
            val state = if (post.totalCount == 0){
                StateCollectData.Error("Ошибка")
            }else{
                StateCollectData.IndiaStock(post, "₹")
            }
            state

        }catch (e: java.lang.Exception){
            e.printStackTrace()
            StateCollectData.Error("Неизвестная ошибка")
        }
    }

    suspend fun collectDataForCrypto(ticket:String): StateCollectData{
        return try {
            val post =postJSONApi.collectDataForShareCrypto(
                PostDTO(
                    columns=listOf("close"),
                    symbols= Symbols(
                        tickers= listOf(ticket),
                        query = Query(
                            types= listOf()
                        )
                    )
                )
            )
            StateCollectData.CryptoStock(post, "$")
        }catch (e: java.lang.Exception){
            e.printStackTrace()
            StateCollectData.Error("Неизвестная ошибка")
        }
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