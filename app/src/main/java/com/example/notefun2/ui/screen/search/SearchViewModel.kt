package com.example.notefun2.ui.screen.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notefun2.data.source.GamesRepository
import com.example.notefun2.domain.model.Game
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val gameListRepository: GamesRepository) :
    ViewModel() {

    private var _searchResult = MutableLiveData<List<Game>>(emptyList())
    val searchResult = _searchResult
    private var _isSearching = mutableStateOf(false)
    val isSearching = _isSearching
    private var _notFound = mutableStateOf(false)
    val notFound = _notFound

    private var lastQuery = ""

    fun search(query: String){
        if(query.isBlank() || lastQuery == query.trim()) return

        _isSearching.value = true

        viewModelScope.launch {
            _searchResult.value = gameListRepository.searchGames(query)
            _notFound.value = _searchResult.value!!.isEmpty()
            _isSearching.value = false
        }
        lastQuery = query.trim()
    }
}