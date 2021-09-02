package com.example.notefun2.ui.screen.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notefun2.data.source.DetailGameRepository
import com.example.notefun2.data.source.GamesRepository
import com.example.notefun2.domain.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameDetailsViewModel @Inject constructor(
    private val gameDetailsRepository: DetailGameRepository,
    private val gameListRepository: GamesRepository,
) : ViewModel() {

    private var isInitialized = false
    private val _game = MutableLiveData<Game>()
    val game: LiveData<Game> = _game

    private val _similarGames = MutableLiveData<List<Game>>(emptyList())
    val similarGames = _similarGames

    fun initialize(gameId: Long) {
        // The `NavHost` call the `composable` function twice the first time, so we prevent fetching
        // the data twice.
        if (isInitialized) return
        isInitialized = true

        viewModelScope.launch {
            _game.value = gameDetailsRepository.getDetailGame(gameId)
            if(game.value!!.similiarGameId.isNotEmpty()){
                _similarGames.value = gameListRepository.getGamesById(game.value!!.similiarGameId)
            }
        }
    }
}