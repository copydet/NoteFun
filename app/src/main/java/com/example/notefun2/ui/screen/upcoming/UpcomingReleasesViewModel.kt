package com.example.notefun2.ui.screen.upcoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gamecore.data.source.GamesRepository
import com.example.gamecore.data.source.remote.network.igdb.provider.ListGamesProvider
import com.example.gamecore.domain.model.Release
import com.example.gamecore.domain.model.enum_class.GameMode
import com.example.gamecore.domain.model.enum_class.GenreGame
import com.example.notefun2.ui.screen.shared.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingReleasesViewModel @Inject constructor(private val gameListRepository: GamesRepository) :
    BaseViewModel() {

    private var _upcomingReleases = MutableLiveData<List<Release>>(emptyList())
    val upcomingReleases = _upcomingReleases

    private var gameGareFilter: GenreGame? = null
    private var gameModeFilter: GameMode? = null

    init {
        fetchNextPage = {
            _upcomingReleases.value =
                _upcomingReleases.value!! + gameListRepository.getUpcomingRelease(
                    page = currentPage++,
                    genreGame = gameGareFilter,
                    gameMode = gameModeFilter,
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
        _upcomingReleases.value = emptyList()
        currentPage = 0
    }
}