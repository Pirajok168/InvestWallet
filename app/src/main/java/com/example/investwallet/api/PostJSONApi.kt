package com.example.investwallet.api

import com.example.investwallet.dto.answer.AnswerDTO
import com.example.investwallet.dto.post.PostDTO
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PostJSONApi {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/america/scan")
    suspend fun collectDataForShareAmerica(@Body dto: PostDTO): AnswerDTO


    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/russia/scan")
    suspend fun collectDataForShareRussia(@Body dto: PostDTO): AnswerDTO

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/crypto/scan")
    suspend fun collectDataForShareCrypto(@Body dto: PostDTO): AnswerDTO
}