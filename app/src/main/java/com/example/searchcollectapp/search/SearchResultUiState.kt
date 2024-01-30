package com.example.searchcollectapp.search

import com.example.searchcollectapp.Document

// 검색 결과 페이지의 UI 상태를 저장하는 데이터 클래스
data class SearchResultUiState(
    val searchResult: List<Document>
) {
    companion object {
        // 처음에는 빈 리스트로 초기화
        fun init() = SearchResultUiState(
            searchResult = emptyList()
        )
    }
}