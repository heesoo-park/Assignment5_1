package com.example.searchcollectapp

data class SearchResultUiState(
    val searchResult: List<Document>
) {
    companion object {
        fun init() = SearchResultUiState(
            searchResult = emptyList()
        )
    }
}