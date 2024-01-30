package com.example.searchcollectapp.storage

import com.example.searchcollectapp.Document

// 내 보관함 페이지의 UI 상태를 저장하는 데이터 클래스
data class StorageResultUiState(
    val selectedResult: List<Document>
) {
    companion object {
        // 처음에는 빈 리스트로 초기화
        fun init() = StorageResultUiState(
            selectedResult = emptyList()
        )
    }
}