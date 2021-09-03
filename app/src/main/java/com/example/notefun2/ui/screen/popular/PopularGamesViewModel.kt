package com.example.notefun2.ui.screen.popular

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gamecore.data.source.GamesRepository
import com.example.gamecore.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.gamecore.domain.model.Game
import com.example.gamecore.domain.model.enum_class.GameMode
import com.example.gamecore.domain.model.enum_class.GenreGame
import com.example.notefun2.ui.screen.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PopularGamesViewModel @Inject constructor(private val gameListRepository: GamesRepository) :
    BaseViewModel() {

    private var _popularGames = MutableLiveData<List<Game>>(emptyList())
    val popularGames = _popularGames

    private var gameGareFilter: GenreGame? = null
    private var gameModeFilter: GameMode? = null
    private var isInitialized = false

    fun initializes(startTimestamp: Long) {
        if (isInitialized) return
        isInitialized = true

        fetchNextPage = {
            _popularGames.value = _popularGames.value!! + gameListRepository.getPopularGames(
                startTimeStamp = startTimestamp,
                endTimeStamp = Calendar.getInstance().timeInMillis, // NOW
                page = currentPage++,
                gameMode = gameModeFilter,
                genreGame = gameGareFilter,
            ).apply {
                isLastPageReached = size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
            }
        }

        getNextPage()
    }

    private fun getNextPage() {
        viewModelScope.launch {
            fetchNextPage()
        }
    }

    override fun resetFilter() {
        resetPages()
        getNextPage()
    }

    override fun filterGameByGameMode(mode: GameMode) {
        resetPages()
        gameModeFilter = mode
        getNextPage()
    }

    override fun filterByGenreGame(genre: GenreGame) {
        resetPages()
        gameGareFilter = genre
        getNextPage()
    }

    private fun resetPages() {
        gameGareFilter = null
        gameModeFilter = null
        _popularGames.value = emptyList()
        currentPage = 0
    }
}