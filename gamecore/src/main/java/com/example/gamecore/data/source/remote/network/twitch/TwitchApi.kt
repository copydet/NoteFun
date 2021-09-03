package com.example.gamecore.data.source.remote.network.twitch

import com.example.gamecore.BuildConfig
import com.example.gamecore.domain.model.token.TwitchApiToken
import retrofit2.http.POST

interface TwitchApi {
    companion object{
       private const val API_TOKEN_URL = "token?client_id=${BuildConfig.CLIENT_ID}&client_secret=${BuildConfig.CLIENT_SECRET}&grant_type=client_credentials"
    }

    @POST(API_TOKEN_URL)
    suspend fun getAccessToken(): TwitchApiToken
}