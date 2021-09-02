package com.example.notefun2.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notefun2.data.source.GamesRepository
import com.example.notefun2.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.notefun2.domain.model.Game
import com.example.notefun2.domain.model.Release
import com.example.notefun2.domain.model.enum_class.GameMode
import com.example.notefun2.domain.model.enum_class.GenreGame
import com.example.notefun2.ui.screen.shared.BaseViewModel
import com.example.notefun2.ui.utils.getYearTimestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.*
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@HiltViewModel
class HomeViewModel @Inject constructor(private val gameRepository: GamesRepository):BaseViewModel() {

    private val minReleaseDate: Long by lazy {
        val numberOfYearsSince2000 = Calendar.getInstance().get(Calendar.YEAR) - 2000
        val randomYearFrom2000toLastYear = Random().nextInt(numberOfYearsSince2000 - 1)+ 2000
        getYearTimestamp(randomYearFrom2000toLastYear)
    }

    private val _gameList: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val gameList: LiveData<List<Game>> = _gameList

    private val _mostPopularGame: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val mostPopularGame: LiveData<List<Game>> = _mostPopularGame

    private val _newGames: MutableLiveData<List<Game>> = MutableLiveData(emptyList())
    val newGames: LiveData<List<Game>> = _newGames

    private val _upcomingReleases: MutableLiveData<List<Release>> = MutableLiveData(emptyList())
    val upcomingRelease: LiveData<List<Release>> = _upcomingReleases

    init {
        fetchNewGames()
        fetchGameList()
        fetchUpcomingGame()

        fetchNextPage = {
            val loadedGames =gameRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimeStamp = getYearTimestamp(),
                page = ++currentPage
            ).sortedByDescending { it.rating }

            isLastPageReached = loadedGames.size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST

            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun fetchNewGames(){
        viewModelScope.launch {
            val threeMonthAgo = Clock.System.now().minus(Duration.Companion.days(30 * 3))
            _newGames.value = gameRepository.getGamesReleaseAfter(
                timeStamp = threeMonthAgo.toEpochMilliseconds(),
                count = 10
            )
        }
    }
    private fun fetchGameList(){
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimeStamp = getYearTimestamp()
            ))
        }
    }

    private fun fetchUpcomingGame(){
        viewModelScope.launch {
            _upcomingReleases.value = gameRepository.getUpcomingRelease(count = 10)
        }
    }

    private fun setPopularAndHighlyRatedGames(gameList: List<Game>){
        _mostPopularGame.value = gameList.subList(0, 10)
        _gameList.value = gameList.subList(10, gameList.size).sortedByDescending { it.rating }
    }

    override fun resetFilter() {
        fetchGameList()
    }

    override fun filterGameByGameMode(mode: GameMode) {
        currentPage = 0
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimeStamp = getYearTimestamp(),
                gameMode = mode
            ))
        }
        fetchNextPage = {
            val loadedGames = gameRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimeStamp = getYearTimestamp(),
                page = ++currentPage,
                gameMode = mode
            ).sortedByDescending { it.rating }

            isLastPageReached = loadedGames.size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }

    override fun filterByGenreGame(genre: GenreGame) {
        currentPage = 0
        viewModelScope.launch {
            setPopularAndHighlyRatedGames(gameRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimeStamp = getYearTimestamp(),
                genreGame = genre
            ))
        }
        fetchNextPage = {
            val loadedGames = gameRepository.getPopularGames(
                startTimeStamp = minReleaseDate,
                endTimeStamp = getYearTimestamp(),
                page = ++currentPage,
                genreGame = genre
            ).sortedByDescending { it.rating }

            isLastPageReached = loadedGames.size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
            _gameList.value = _gameList.value!!.plus(loadedGames)
        }
    }
}