package com.example.gamecore.di

import com.example.gamecore.data.source.remote.network.igdb.ServiceClientIGDB
import com.example.gamecore.data.source.remote.network.igdb.provider.GameDetailProvider
import com.example.gamecore.data.source.remote.network.igdb.provider.ListGamesProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SingletonModuleForInterfaces {

    @Binds
    abstract fun bindsGameList(apiIgdb: ServiceClientIGDB): ListGamesProvider

    @Binds
    abstract fun bindsDetailGame(apiIgdb: ServiceClientIGDB): GameDetailProvider
}