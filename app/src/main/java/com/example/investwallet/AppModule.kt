package com.example.investwallet

import android.content.Context
import androidx.room.Room
import com.example.investwallet.api.JSONHeadlinesAPI
import com.example.investwallet.api.JSONSearchApi
import com.example.investwallet.api.PostJSONApi
import com.example.investwallet.database.UserDatabase
import com.example.investwallet.dto.converter.Content
import com.example.investwallet.dto.converter.ContentHolderTypeAdapter
import com.example.investwallet.repository.ApiRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @Named("databaseUser")
    fun provideDatabaseUser(
        @ApplicationContext context: Context
    ): UserDatabase = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        "user_table"
    ).build()


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
    @Named("apiJSONHeadlines")
    fun provideJSONHeadlinesApi(): JSONHeadlinesAPI = Retrofit.Builder()
        .baseUrl("https://news-headlines.tradingview.com/")
        .client(OkHttpClient())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .registerTypeAdapter(
                        Content::class.java,
                        ContentHolderTypeAdapter()
                    )
                    .create()
            )
        )
        .build()
        .create(JSONHeadlinesAPI::class.java)



    @Singleton
    @Provides
    @Named("postApi")
    fun provideJSONPostApo(): PostJSONApi =Retrofit.Builder()
        .baseUrl("https://scanner.tradingview.com/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PostJSONApi::class.java)


    @Singleton
    @Provides
    fun provideApiRepository(
        @Named("apiJSONSearch") apiJSONSearch: JSONSearchApi,
        @Named("apiJSONHeadlines") apiJSONHeadlines: JSONHeadlinesAPI,
        @Named("databaseUser") databaseUserDatabase: UserDatabase,
        @Named("postApi") postJSONApi: PostJSONApi
    ): ApiRepository = ApiRepository(apiJSONSearch,apiJSONHeadlines, databaseUserDatabase,postJSONApi)

}