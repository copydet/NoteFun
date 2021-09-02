package com.example.notefun2.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.request.IGDBWrapper
import com.example.notefun2.BuildConfig
import com.example.notefun2.data.source.remote.network.igdb.ServiceClientIGDB
import com.example.notefun2.data.source.remote.network.twitch.TokenProviderClient
import com.example.notefun2.data.source.remote.network.twitch.TwitchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Provides
    fun providesIGDBAPIClient(accessToken : TokenProviderClient): ServiceClientIGDB{
        return ServiceClientIGDB(IGDBWrapper, APICalypse(), accessToken)
    }

    @Provides
    fun prodvidesAccessToken(@ApplicationContext context: Context, twitchClient : TwitchApi): TokenProviderClient{
        val sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        return TokenProviderClient(twitchClient, sharedPreferences)
    }

    @Provides
    operator fun invoke(): TwitchApi {
            val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES).build()
            return Retrofit.Builder()
                .baseUrl(BuildConfig.TWICH_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TwitchApi::class.java)
        }

}