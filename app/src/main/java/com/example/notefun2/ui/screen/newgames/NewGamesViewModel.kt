package com.example.notefun2.ui.screen.newgames

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notefun2.data.source.GamesRepository
import com.example.notefun2.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.notefun2.domain.model.Game
import com.example.notefun2.domain.model.enum_class.GameMode
import com.example.notefun2.domain.model.enum_class.GenreGame
import com.example.notefun2.ui.screen.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewGamesViewModel @Inject constructor(private val gameRepository: GamesRepository): BaseViewModel() {

    private var isInitialized = false
    private var minReleaseStamp = 0L

    private var _newGames = MutableLiveData<List<Game>>()
    val newGame : LiveData<List<Game>> = _newGames

    private var genreFilter : GenreGame? = null
    private var modeFilter : GameMode? = null

    init {
        fetchNextPage = {
            _newGames.value =
                _newGames.value!! + gameRepository.getGamesReleaseAfter(
                    minReleaseStamp,
                    page = currentPage++,
                    genreGame = genreFilter,
                    gameMode = modeFilter,
                ).apply {
                    isLastPageReached = size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
                }.sortedByDescending { it.rating }
        }
    }

    fun initialize(minReleaseTimeStamp: Long){
        if (isInitialized) return
        isInitialized = true

        this.minReleaseStamp = minReleaseTimeStamp
        fetchNewGames()
    }
    private fun fetchNewGames(){
        viewModelScope.launch {
            _newGames.value =
                gameRepository.getGamesReleaseAfter(minReleaseStamp, currentPage).apply {
                    isLastPageReached = size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
                }
        }
    }

    override fun resetFilter() {
        currentPage = 0
        _newGames.value = emptyList()
        fetchNewGames()
    }

    override fun filterGameByGameMode(mode: GameMode) {
        currentPage = 0
        fetchNextPage = {
            _newGames.value =
                _newGames.value!!.plus(
                    gameRepository.getGamesReleaseAfter(
                        minReleaseStamp,
                        currentPage++,
                        gameMode = mode
                    ).apply {
                        isLastPageReached = size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
                    }
                )
        }
        _newGames.value = emptyList()
        viewModelScope.launch {
            fetchNextPage()
        }
    }

    override fun filterByGenreGame(genre: GenreGame) {
        currentPage = 0
        fetchNextPage ={
            _newGames.value =
                _newGames.value!!.plus(
                    gameRepository.getGamesReleaseAfter(
                        minReleaseStamp,
                        currentPage++,
                        genreGame = genre
                    ).apply {
                        isLastPageReached = size < ListGamesProvider.DEFAULT_GAME_COUNT_BY_REQUEST
                    }
                )
        }
        _newGames.value = emptyList()
        viewModelScope.launch {
            fetchNextPage()
        }
    }
}