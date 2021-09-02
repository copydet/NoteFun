package com.example.notefun2.data.source.remote.network.twitch

import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class TokenProviderClient(
    private val tokenClient: TwitchApi,
    private val sharedPreferences: SharedPreferences
) {

    /**
     * Mengambil Access token dari Twitch dan Menyimpannya di SharedPreference
     *
     * Cara ini masih dalam tahap percobaan
     */

    private val accessTokenCache = "ACCESS_TOKEN"
    private val expiresTokenCache = "EXPIRES_TOKEN"

    private var accessToken : String = sharedPreferences.getString(accessTokenCache, "")!!
    private var expiresToken : Long = sharedPreferences.getLong(expiresTokenCache, 0)

    suspend fun getAccessToken():String{
        if (accessToken.isEmpty()){
            accessToken = fetchFromServerAndSave().access_token
        }
        return accessToken
    }

    suspend fun fetchFromServerAndSave() = fetchTokenFromServer().also{
        accessToken = it.access_token
        expiresToken = it.expires_in
        sharedPreferences.edit().putString(accessTokenCache, it.access_token).apply()
        sharedPreferences.edit().putLong(expiresTokenCache, it.expires_in).apply()
    }

    /* Digunakan untuk mereset Token
    fun resetToken() {
        sharedPreferences.edit().remove(accessTokenCacheKey).apply()
    }
     */

    private suspend fun fetchTokenFromServer() = withContext(IO) {
        tokenClient.getAccessToken()
    }
}