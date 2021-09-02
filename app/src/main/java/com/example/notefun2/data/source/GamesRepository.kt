package com.example.notefun2.data.source

import com.example.notefun2.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.notefun2.domain.model.Game
import com.example.notefun2.domain.model.Release
import com.example.notefun2.domain.model.enum_class.GameMode
import com.example.notefun2.domain.model.enum_class.GenreGame
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesRepository @Inject constructor(
    private val listGamesProvider: ListGamesProvider
) {

    suspend fun getPopularGames(
        startTimeStamp:Long,
        endTimeStamp: Long,
        page: Int = 0,
        count: Int = ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
        genreGame: GenreGame? = null,
        gameMode: GameMode? = null
    ): List<Game>{
        return listGamesProvider.getPopularGames(startTimeStamp, endTimeStamp, page, count, genreGame, gameMode)
    }

    suspend fun getGamesReleaseAfter(
        timeStamp: Long,
        page: Int = 0,
        count: Int = ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
        genreGame: GenreGame? = null,
        gameMode: GameMode? = null
    ): List<Game>{
        return listGamesProvider.getGamesReleasedAfter(timeStamp, page, count, genreGame, gameMode)
    }

    suspend fun getUpcomingRelease(
        limitTimeStamp: Long = 0,
        page: Int = 0,
        count: Int = ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST,
        genreGame: GenreGame? = null,
        gameMode: GameMode? = null
    ): List<Release>{
        return listGamesProvider.getUpcomingRelease(limitTimeStamp, page, count, genreGame, gameMode)
    }

    suspend fun getGamesById(gameId: List<Long>): List<Game>{
        return listGamesProvider.getGamesById(gameId)
    }

    suspend fun searchGames(query: String): List<Game>{
        return listGamesProvider.searchGames(query)
    }
}