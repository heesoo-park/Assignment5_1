package com.example.searchcollectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    private val _searchUiState: MutableLiveData<SearchResultUiState> = MutableLiveData(SearchResultUiState.init())
    val searchUiState: LiveData<SearchResultUiState> get() = _searchUiState

    private val _favoriteResult: MutableLiveData<FavoriteResultUiState> = MutableLiveData(FavoriteResultUiState.init())
    val favoriteResult: LiveData<FavoriteResultUiState> get() = _favoriteResult

    fun changeMarker(selectedResult: Document) {
        if (searchUiState.value?.searchResult?.find { it == selectedResult } != null) {
            _searchUiState.value = searchUiState.value?.copy(
                searchResult = searchUiState.value?.searchResult.orEmpty().toMutableList().apply {
                    set(indexOf(find { it == selectedResult }), selectedResult.copy(
                        isLiked = !selectedResult.isLiked
                    ))
                }
            )
        }
    }

    fun registerSearchResult(results: List<Document>) {
        _searchUiState.value = SearchResultUiState(results)
    }

    fun registerFavoriteResult(selectedResult: Document) {
        if (favoriteResult.value?.favoriteResult?.find { it == selectedResult } != null) {
            _favoriteResult.value = FavoriteResultUiState(favoriteResult.value?.favoriteResult.orEmpty().toMutableList().apply {
                removeIf { it == selectedResult }
            })
        } else {
            _favoriteResult.value = favoriteResult.value?.copy(
                favoriteResult = favoriteResult.value?.favoriteResult.orEmpty().toMutableList().apply {
                    add(selectedResult.copy(
                        isLiked = !selectedResult.isLiked
                    ))
                    sortByDescending { it.dateTime }
                }
            )
        }
    }
}