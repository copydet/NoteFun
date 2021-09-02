package com.example.notefun2.data.source.remote.network.igdb.provider

import com.example.notefun2.domain.model.Game

interface GameDetailProvider {

    /**
     * mengambil data untuk detail game
     */
    suspend fun getDetailGames(gameId: Long): Game
}