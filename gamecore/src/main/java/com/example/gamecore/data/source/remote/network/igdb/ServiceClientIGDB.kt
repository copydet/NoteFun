package com.example.gamecore.data.source.remote.network.igdb

import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.apicalypse.Sort
import com.api.igdb.exceptions.RequestException
import com.api.igdb.request.IGDBWrapper
import com.api.igdb.request.games
import com.api.igdb.request.releaseDates
import com.api.igdb.utils.ImageSize
import com.example.gamecore.BuildConfig
import com.example.gamecore.data.source.remote.network.igdb.provider.GameDetailProvider
import com.example.gamecore.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.notefun2.data.source.remote.network.toDomainGame
import com.example.notefun2.data.source.remote.network.toDomainRelease
import com.example.gamecore.data.source.remote.network.twitch.TokenProviderClient
import com.example.gamecore.domain.model.Game
import com.example.gamecore.domain.model.Release
import com.example.gamecore.domain.model.enum_class.GameMode
import com.example.gamecore.domain.model.enum_class.GenreGame
import com.example.gamecore.utils.toLowerCaseExceptFirstChar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import proto.ReleaseDate
import java.util.*

class ServiceClientIGDB(
    private val igdbWrapper: IGDBWrapper,
    private val apiCalypse: APICalypse,
    private val accessTokenProvider: TokenProviderClient
): ListGamesProvider, GameDetailProvider {

    init {
        val threadLock = Object()
        CoroutineScope(IO).launch {
            val accessToken = accessTokenProvider.getAccessToken()
            igdbWrapper.setCredentials(BuildConfig.CLIENT_ID, accessToken)

            synchronized(threadLock){
                threadLock.notify()
            }
        }
        synchronized(threadLock){
            threadLock.wait(3000)
        }
    }

    private var numberRetriedRequest = 0

    private val fieldGame = "name, cover.image_id, total_rating, platforms.name, screenshots.image_id, status, artworks.image_id, total_rating_count"

    private val fieldRelease = "date, region, game.name, game.cover.image_id, platform.name, game.screenshots.image_id, game.artworks.image_id"

    private val fieldGameComplete = "$fieldGame, genres.name, game_modes.name, age_ratings.rating, first_release_date, themes.name, websites.category, websites.url," +
            "player_perspectives.name, release_dates.date, release_dates.region, release_dates.platform.name, similar_games, storyline, summary, videos.video_id, videos.name," +
            "total_rating_count, involved_companies.developer, involved_companies.publisher, involved_companies.company.name," +
            "involved_companies.company.description, involved_companies.company.country, involved_companies.company.logo.image_id"

    override suspend fun getDetailGames(gameId: Long): Game {
        val queryBuilder = apiCalypse.newBuilder().fields(fieldGameComplete)
            .where("id = $gameId")

        return withContext(IO){
            try {
                igdbWrapper.games(queryBuilder).first().toDomainGame(ImageSize.HD).also {
                    numberRetriedRequest = 0
                }
            } catch (e: RequestException){


                when{
                    e.statusCode == 429 && numberRetriedRequest <= 3 -> {
                        numberRetriedRequest++
                        getDetailGames(gameId)
                    }

                    e.statusCode == 401 -> {
                        igdbWrapper.setCredentials(
                            BuildConfig.CLIENT_ID,
                            accessTokenProvider.fetchFromServerAndSave().access_token,
                        )
                        getDetailGames(gameId)
                    }
                    else -> {
                        numberRetriedRequest = 0
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun getPopularGames(
        startTimeStamp: Long,
        endTimeStamp: Long,
        page: Int,
        count: Int,
        gameGenre: GenreGame?,
        gameMode: GameMode?
    ): List<Game> {
        val numberOfGamesToFetch = regularizeGameCount(count)
        val queryBuilder = apiCalypse.newBuilder()
            .fields(fieldGame)
            .where(
                "first_release_date > ${startTimeStamp.toFixed10Digits()} & first_release_date < ${endTimeStamp.toFixed10Digits()} " +
                        "${getGameGenreQuery(gameGenre)}  ${getGameModeQuery(gameMode)} & total_rating_count != 0",
            )
            .sort("total_rating_count", Sort.DESCENDING)
            .limit(numberOfGamesToFetch)
            .offset(page * count)

        return withContext(IO){
            try {
                igdbWrapper.games(queryBuilder).map(proto.Game::toDomainGame).also {
                    numberRetriedRequest = 0
                }
            } catch (e: RequestException){


                when{
                    e.statusCode == 429 && numberRetriedRequest <= 3 -> {
                        numberRetriedRequest++
                        getPopularGames(startTimeStamp, endTimeStamp, page, count, gameGenre, gameMode)
                    }

                    e.statusCode == 401 -> {
                        igdbWrapper.setCredentials(
                            BuildConfig.CLIENT_ID,
                            accessTokenProvider.fetchFromServerAndSave().access_token,
                        )
                        getPopularGames(startTimeStamp, endTimeStamp, page, count, gameGenre, gameMode)
                    }
                    else -> {
                        numberRetriedRequest = 0
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun getGamesReleasedAfter(
        timeStamp: Long,
        page: Int,
        count: Int,
        gameGenre: GenreGame?,
        gameMode: GameMode?
    ): List<Game> {
        val todayTimeStamp = Calendar.getInstance().timeInMillis
        val numberOfGameToFecth = regularizeGameCount(count)
        val queryBuilder = apiCalypse.newBuilder()
            .fields(fieldGame)
            .where(
                "first_release_date > ${timeStamp.toFixed10Digits()} & first_release_date < ${todayTimeStamp.toFixed10Digits()}" +
                        " & platforms = (${getIGDBPlatformID()}) ${getGameGenreQuery(gameGenre)}  ${
                            getGameModeQuery(
                                gameMode
                            )
                        } & total_rating_count != 0",
            )
            .sort("total_rating_count", Sort.DESCENDING)
            .limit(numberOfGameToFecth)
            .offset(page * count)

        return withContext(IO) {
            try {
                igdbWrapper.games(queryBuilder).map(proto.Game::toDomainGame).also {
                    numberRetriedRequest = 0 // reset on success
                }
            } catch (e: RequestException) {
                // TODO REFACTOR
                // The server will return et response with status code 429 if it receive more than 4
                // request by second, if that happen we retry again and again 3 times.
                when {
                    e.statusCode == 429 && numberRetriedRequest <= 3 -> {
                        numberRetriedRequest++
                        getGamesReleasedAfter(timeStamp, page, count, gameGenre, gameMode)
                    }

                    // When the access token is invalid the server return a response with status code 401.
                    e.statusCode == 401 -> {
                        igdbWrapper.setCredentials(
                            BuildConfig.CLIENT_ID,
                            accessTokenProvider.fetchFromServerAndSave().access_token
                        )
                        getGamesReleasedAfter(timeStamp, page, count, gameGenre, gameMode)
                    }

                    else -> {
                        numberRetriedRequest = 0
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun getUpcomingRelease(
        limitTimeStamp: Long,
        page: Int,
        count: Int,
        gameGenre: GenreGame?,
        gameMode: GameMode?
    ): List<Release> {
        val numberOfGamesToFecth = regularizeGameCount(count)
        val todayTimeStamp = Calendar.getInstance().timeInMillis

        var maxReleaseDateQuery = ""
        if (limitTimeStamp > 0){
            maxReleaseDateQuery = "& first_release_date < ${limitTimeStamp.toFixed10Digits()}"
        }
        val queryBuilder = apiCalypse.newBuilder()
            .fields(fieldRelease)
            .where(
                "date >= ${todayTimeStamp.toFixed10Digits()} $maxReleaseDateQuery & game.platforms = (${getIGDBPlatformID()})" +
                        "${getGameGenreQuery(gameGenre, "game.")} ${getGameModeQuery(gameMode,"game.")}"
            )
            .sort("date", Sort.ASCENDING)
            .limit(numberOfGamesToFecth)
            .offset(page * count)

        return withContext(IO) {
            try {
                igdbWrapper.releaseDates(queryBuilder).map(ReleaseDate::toDomainRelease).also {
                    numberRetriedRequest = 0
                }
            } catch (e: RequestException){
                when {
                    e.statusCode == 429 && numberRetriedRequest <= 3 -> {
                        numberRetriedRequest++
                        getUpcomingRelease(limitTimeStamp, page, count, gameGenre, gameMode)
                    }

                    e.statusCode == 401 -> {
                        igdbWrapper.setCredentials(
                            BuildConfig.CLIENT_ID,
                            accessTokenProvider.fetchFromServerAndSave().access_token
                        )
                        getUpcomingRelease(limitTimeStamp, page, count, gameGenre, gameMode)
                    }
                    else -> {
                        numberRetriedRequest = 0
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun getGamesById(gamesId: List<Long>): List<Game> {
        if (gamesId.isEmpty()) return emptyList()
        val queryBuilder = apiCalypse.newBuilder().fields(fieldGame)
            .where("id = (${gamesId.joinToString()})")

        return withContext(IO){
            try {
                igdbWrapper.games(queryBuilder).map(proto.Game::toDomainGame).also {
                    numberRetriedRequest = 0
                }
            }catch (e: RequestException){
                when{
                    e.statusCode == 429 && numberRetriedRequest <= 3 -> {
                        numberRetriedRequest++
                        getGamesById(gamesId)
                    }
                    e.statusCode == 401 -> {
                        igdbWrapper.setCredentials(
                            BuildConfig.CLIENT_ID,
                            accessTokenProvider.fetchFromServerAndSave().access_token
                        )
                        getGamesById(gamesId)
                    }
                    else -> {
                        numberRetriedRequest = 0
                        throw e
                    }
                }
            }
        }
    }

    override suspend fun searchGames(query: String): List<Game> {
        val queryBuilder = apiCalypse.newBuilder()
            .fields("name, cover.image_id, platforms.name")
            .search(query)
            .where("version_parent = null & first_release_date != null")

        return withContext(IO){
            try {
                igdbWrapper.games(queryBuilder).map(proto.Game::toDomainGame).also {
                    numberRetriedRequest = 0
                }
            } catch (e: RequestException){
                when{
                    e.statusCode == 429 && numberRetriedRequest <= 3 -> {
                        numberRetriedRequest++
                        searchGames(query)
                    }
                    e.statusCode == 401 -> {
                        igdbWrapper.setCredentials(
                            BuildConfig.CLIENT_ID,
                            accessTokenProvider.fetchFromServerAndSave().access_token
                        )
                        searchGames(query)
                    }
                    else -> {
                        numberRetriedRequest = 0
                        throw e
                    }
                }
            }
        }
    }
}


//---------------------------------------------------------//
fun APICalypse.newBuilder() = APICalypse()
fun Long.toFixed10Digits(): String {
    val thisNumberAsString = this.toString()
    if (thisNumberAsString.length > 10){
        return thisNumberAsString.substring(0..9)
    }
    return thisNumberAsString
}
fun GenreGame.igdbCompatibleName()=
    if (this == GenreGame.RPG) "Role-playing (RPG)"
    else name.toLowerCaseExceptFirstChar()