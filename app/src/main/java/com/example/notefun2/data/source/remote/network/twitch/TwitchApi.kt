package com.example.notefun2.data.source.remote.network.twitch

import com.example.notefun2.BuildConfig
import com.example.notefun2.domain.model.token.TwitchApiToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface TwitchApi {
    companion object{
       private const val API_TOKEN_URL = "token?client_id=${BuildConfig.CLIENT_ID}&client_secret=${BuildConfig.CLIENT_SECRET}&grant_type=client_credentials"
    }

    @POST(API_TOKEN_URL)
    suspend fun getAccessToken(): TwitchApiToken
}