package com.example.notefun2.data.source

import com.example.notefun2.data.source.remote.network.igdb.provider.GameDetailProvider
import com.example.notefun2.domain.model.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailGameRepository @Inject constructor(
    private val gameDetailProvider: GameDetailProvider
){

    suspend fun getDetailGame(gameId: Long): Game{
        return gameDetailProvider.getDetailGames(gameId)
    }
}