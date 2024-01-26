package com.example.searchcollectapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _searchUiState: MutableLiveData<SearchResultUiState> = MutableLiveData(SearchResultUiState.init())
    val searchUiState: LiveData<SearchResultUiState> get() = _searchUiState

    private val _favoriteResult: MutableLiveData<FavoriteResultUiState> = MutableLiveData(FavoriteResultUiState.init())
    val favoriteResult: LiveData<FavoriteResultUiState> get() = _favoriteResult

    fun registerSearchResult(results: ArrayList<Document>) {
        _searchUiState.value = SearchResultUiState(results)
    }

    fun registerFavoriteResult(selectedResult: Document) {
        _favoriteResult.value = if (favoriteResult.value?.favoriteResult?.find { it == selectedResult } != null) {
            favoriteResult.value?.copy(
                favoriteResult = favoriteResult.value?.favoriteResult.orEmpty().toMutableList().apply {
                    add(selectedResult)
                    sortBy { it.dateTime }
                })
        } else {
            favoriteResult.value?.copy(
                favoriteResult = favoriteResult.value?.favoriteResult.orEmpty().toMutableList().apply {
                    removeIf { it == selectedResult }
                    sortBy { it.dateTime }
                })
        }
    }
}