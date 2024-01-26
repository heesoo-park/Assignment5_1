package com.example.searchcollectapp

data class FavoriteResultUiState(
    val favoriteResult: List<Document>
) {
    companion object {
        fun init() = FavoriteResultUiState(
            favoriteResult = emptyList()
        )
    }
}