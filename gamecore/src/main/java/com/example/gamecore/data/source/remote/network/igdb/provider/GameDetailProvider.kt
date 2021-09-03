package com.example.gamecore.data.source.remote.network.igdb.provider

import com.example.gamecore.domain.model.Game

interface GameDetailProvider {

    /**
     * mengambil data untuk detail game
     */
    suspend fun getDetailGames(gameId: Long): Game
}