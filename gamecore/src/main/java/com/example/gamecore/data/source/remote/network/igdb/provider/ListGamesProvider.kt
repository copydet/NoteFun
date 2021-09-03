package com.example.gamecore.data.source.remote.network.igdb.provider

import com.example.gamecore.domain.model.Game
import com.example.gamecore.domain.model.Release
import com.example.gamecore.domain.model.enum_class.GameMode
import com.example.gamecore.domain.model.enum_class.GenreGame

interface ListGamesProvider {
    companion object{
        const val MAX_GAMES_COUNT_BY_REQUEST = 100
        const val DEFAULT_GAME_COUNT_BY_REQUEST = 30
    }

    /**
     * Mengembalikan PopularGames dengan interval dari ([startTimeStamp] ke [endTimeStamp]).
     */
    suspend fun getPopularGames(
        startTimeStamp: Long,
        endTimeStamp: Long,
        page: Int = 0,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GenreGame? = null,
        gameMode: GameMode? = null
    ) : List<Game>

    /**
     * return the game that have been released after the given [timeStamp].
     *
     */

    suspend fun getGamesReleasedAfter(
        timeStamp: Long,
        page: Int = 0,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GenreGame? = null,
        gameMode: GameMode? = null
    ): List<Game>

    /**
     * Return the [Releases] that will happen before the given [limitTimeStamp] and after the current
     * date plus one day, if no [limitTimeStamp] is given, there will not be a limit date.
     */

    suspend fun getUpcomingRelease(
        limitTimeStamp: Long = 0,
        page: Int = 0,
        count: Int = DEFAULT_GAME_COUNT_BY_REQUEST,
        gameGenre: GenreGame? = null,
        gameMode: GameMode? = null
    ): List<Release>

    /**
     * Mengambil data Game dengan [gamesId]
     */
    suspend fun getGamesById(gamesId: List<Long>): List<Game>

    /**
     * function for search games by [query]
     */
    suspend fun searchGames(query: String): List<Game>

}