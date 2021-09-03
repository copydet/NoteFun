package com.example.gamecore.data.source

import com.example.gamecore.data.source.remote.network.igdb.provider.GameDetailProvider
import com.example.gamecore.domain.model.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailGameRepository @Inject constructor(
    private val gameDetailProvider: GameDetailProvider
){

    suspend fun getDetailGame(gameId: Long): Game {
        return gameDetailProvider.getDetailGames(gameId)
    }
}