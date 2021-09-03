package com.example.notefun2.ui.screen.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamecore.domain.model.enum_class.GameMode
import com.example.gamecore.domain.model.enum_class.GenreGame
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(){
    protected var currentPage = 0
    protected lateinit var fetchNextPage: suspend () -> Unit
    var isLastPageReached by mutableStateOf(false)
        protected set
    var isNextPageLoading by mutableStateOf(false)
        private set

    fun onGameTypeSelected(genre: String){
        when(genre){
            "ALL"-> resetFilter()
            "MULTIPLAYER"-> filterGameByGameMode(GameMode.MULTIPLAYER)
            "BATTLE ROYALE" -> filterGameByGameMode(GameMode.BATTLE_ROYALE)
            "SINGLE PLAYER" -> filterGameByGameMode(GameMode.SINGLE_PLAYER)
            else -> filterByGenreGame(GenreGame.valueOf(genre))
        }
    }

    protected abstract fun resetFilter()

    protected abstract fun filterGameByGameMode(mode: GameMode)

    protected abstract fun filterByGenreGame(genre: GenreGame)

    fun loadNextPage(){
        if (isLastPageReached) return

        viewModelScope.launch {
            isNextPageLoading = true
            fetchNextPage()
            isNextPageLoading = false
        }
    }

}