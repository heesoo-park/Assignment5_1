package com.example.searchcollectapp.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchcollectapp.Document
import com.example.searchcollectapp.NetworkClient
import com.example.searchcollectapp.UseConstant.KEY_SEARCH_WORD
import com.example.searchcollectapp.UseConstant.KEY_SELECT_DATASET
import com.example.searchcollectapp.storage.StorageResultUiState
import com.example.searchcollectapp.search.SearchResultUiState
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MainViewModel(
    private val prefSharedPreferences: SharedPreferences,
    private val favoriteSharedPreferences: SharedPreferences
) : ViewModel() {

    private var page = 0

    // 검색 결과 페이지에 나오는 데이터를 저장하는 라이브 데이터
    private val _searchUiState: MutableLiveData<SearchResultUiState> = MutableLiveData(
        SearchResultUiState.init()
    )
    val searchUiState: LiveData<SearchResultUiState> get() = _searchUiState

    // 내 보관함 페이지에 나오는 데이터를 저장하는 라이브 데이터
    private val _storageUiState: MutableLiveData<StorageResultUiState> = MutableLiveData(
        StorageResultUiState.init()
    )
    val storageUiState: LiveData<StorageResultUiState> get() = _storageUiState

    // 마지막 검색어를 저장하는 라이브 데이터
    private val _lastWord: MutableLiveData<String> = MutableLiveData()
    val lastWord: LiveData<String> get() = _lastWord

    // 필터 타입을 저장하는 라이브 데이터
    private val _type: MutableLiveData<Int> = MutableLiveData(0)
    val type: LiveData<Int> get() = _type

    // 마지막 검색어를 업데이트하는 함수
    private fun updateLastWord(word: String) {
        _lastWord.value = word
    }

    // 선택표시를 컨트롤하는 함수
    fun controlMarker(selectedResult: Document) {
        val list = searchUiState.value?.searchResult

        // 현재 검색 결과 데이터에서 선택된 값이 있을 때
        if (list?.find { it == selectedResult } != null) {
            _searchUiState.value = searchUiState.value?.copy(
                searchResult = list.toMutableList()
                    .apply {
                        set(
                            indexOf(find { it == selectedResult }),
                            when (selectedResult) {
                                // 선택된 값이 이미지 데이터라면
                                is Document.ImageDocument -> {
                                    // 해당 데이터의 선택 플래그를 뒤집음
                                    selectedResult.copy(
                                        isSelected = !selectedResult.isSelected
                                    )
                                }
                                // 선택된 값이 비디오 데이터라면
                                is Document.VideoDocument -> {
                                    // 해당 데이터의 선택 플래그를 뒤집음
                                    selectedResult.copy(
                                        isSelected = !selectedResult.isSelected
                                    )
                                }
                            }
                        )
                    }
            )
        }
    }

    // 검색 결과 페이지에서 검색 결과를 띄울 때 선택된 값이 있는지 체크하고 표시해주는 함수
    private fun setSelectSearchResult() {
        val storageList = storageUiState.value?.selectedResult
        val searchList = searchUiState.value?.searchResult

        // 선택된 데이터들이 존재하지 않는다면 return
        if (storageList.isNullOrEmpty()) return

        storageList.forEach { document ->
            // 검색 결과 데이터를 돌면서
            searchList?.forEach {
                // 현재 데이터와 들어온 데이터가 모두 이미지 데이터일 때
                if (it is Document.ImageDocument && document is Document.ImageDocument) {
                    // imageUrl을 비교하여 같다면 해당 데이터의 선택 플래그를 뒤집음
                    if (it.imageUrl == document.imageUrl) {
                        _searchUiState.value = searchUiState.value?.copy(
                            searchResult = searchList.toMutableList()
                                .apply {
                                    set(
                                        indexOf(it),
                                        it.copy(isSelected = !it.isSelected)
                                    )
                                }
                        )
                    }
                } else if (it is Document.VideoDocument && document is Document.VideoDocument) { // 현재 데이터와 들어온 데이터가 모두 비디오 데이터일 때
                    // url을 비교하여 같다면 해당 데이터의 선택 플래그를 뒤집음
                    if (it.url == document.url) {
                        _searchUiState.value = searchUiState.value?.copy(
                            searchResult = searchList.toMutableList()
                                .apply {
                                    set(
                                        indexOf(it),
                                        it.copy(isSelected = !it.isSelected)
                                    )
                                }
                        )
                    }
                }
            }
        }
    }

    // 서버에서 가져온 처음 데이터를 최신 날짜 순으로 정렬한 다음 라이브 데이터에 등록시키는 함수
    fun registerSearchResult(results: List<Document>) {
        val sortedResults = results.toMutableList().sortedByDescending { result ->
            when (result) {
                is Document.ImageDocument -> result.dateTime
                is Document.VideoDocument -> result.dateTime
            }
        }
        _searchUiState.value = searchUiState.value?.copy(
            searchResult = sortedResults
        )
        setSelectSearchResult()
    }

    // 서버에서 가져온 추가 데이터를 최신 날짜 순으로 정렬한 다음 라이브 데이터에 붙이는 함수
    fun connectSearchResult(results: List<Document>) {
        val sortedResults = results.toMutableList().sortedByDescending { result ->
            when (result) {
                is Document.ImageDocument -> result.dateTime
                is Document.VideoDocument -> result.dateTime
            }
        }
        _searchUiState.value = searchUiState.value?.copy(
            searchResult = searchUiState.value?.searchResult.orEmpty().toMutableList().apply {
                addAll(sortedResults)
            }
        )
        setSelectSearchResult()
    }

    // 내 보관함 페이지에 보여질 데이터를 저장하는 라이브 데이터를 컨트롤하는 함수
    fun manageSelectedDocument(selectedResult: Document) {
        val list = storageUiState.value?.selectedResult

        // 현재 라이브 데이터에 들어온 값과 동일한 값이 있다면
        if (list?.find { it == selectedResult } != null) {
            // 그 데이터를 삭제
            _storageUiState.value = StorageResultUiState(
                list.toMutableList().apply {
                    removeIf { it == selectedResult }
                })
        } else { // 현재 라이브 데이터에 들어온 값과 동일한 값이 없다면
            _storageUiState.value = storageUiState.value?.copy(
                selectedResult = list.orEmpty().toMutableList()
                    .apply {
                        add(
                            when (selectedResult) {
                                // 이미지 데이터일 때
                                is Document.ImageDocument -> {
                                    // 들어온 값의 선택 플래그를 뒤집고 라이브 데이터에 추가한 다음 최신 날짜 순으로 정렬
                                    selectedResult.copy(
                                        isSelected = !selectedResult.isSelected
                                    )
                                }
                                // 비디오 데이터일 때
                                is Document.VideoDocument -> {
                                    // 들어온 값의 선택 플래그를 뒤집고 라이브 데이터에 추가한 다음 최신 날짜 순으로 정렬
                                    selectedResult.copy(
                                        isSelected = !selectedResult.isSelected
                                    )
                                }
                            }
                        )
                        sortByDescending { result ->
                            when (result) {
                                is Document.ImageDocument -> result.dateTime
                                is Document.VideoDocument -> result.dateTime
                            }
                        }
                    }
            )
        }
    }

    // JSON 데이터를 List타입으로 변환시켜 내 보관함 페이지에 보여질 데이터를 저장하는 라이브 데이터에 저장하는 함수
    fun restoreFavoriteData(results: List<Document>) {
        _storageUiState.value = StorageResultUiState(results)
    }

    // sharedPreferences에 저장된 마지막 검색어를 불러오는 함수
    fun loadWordSharedPreferences() {
        updateLastWord(prefSharedPreferences.getString(KEY_SEARCH_WORD, "") ?: "")
    }

    // sharedPreferences에 저장된 내 보관함 데이터들을 불러오는 함수
    fun loadDataSharedPreferences() {
        val favoriteData =
            favoriteSharedPreferences.getString(KEY_SELECT_DATASET, "")

        if (favoriteData != "") {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(Document::class.java, DocumentTypeAdapter())

            restoreFavoriteData(gsonBuilder.create().fromJson(favoriteData, object : TypeToken<List<Document>>() {}.type))
        }
    }

    // 현재 보관해놓은 데이터들을 JSON 데이터로 변환해 sharedPreferences에 저장하는 함수
    fun saveDataSharedPreferences() {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(Document::class.java, DocumentTypeAdapter())
        val editor = favoriteSharedPreferences.edit()
        val favoriteData = gsonBuilder.create().toJson(storageUiState.value?.selectedResult)
        editor.putString(KEY_SELECT_DATASET, favoriteData)
        editor.apply()
    }

    // 마지막 검색어를 sharedPreferences에 저장하는 함수
    fun saveWordSharedPreferences() {
        val edit = prefSharedPreferences.edit()
        edit.putString(KEY_SEARCH_WORD, lastWord.value)
        edit.apply()
    }

    // 필터타입에 맞춰서 서버로부터 데이터 통신하는 함수
    fun communicationNetwork(word: String, page: Int, type: Int) = viewModelScope.launch {
        val param = setUpSearchParameter(word, page.toString())
        val response = arrayListOf<Document>()

        when (type) {
            0 -> response.addAll(
                NetworkClient.searchNetwork.searchImage(param).imageDocuments.orEmpty() +
                        NetworkClient.searchNetwork.searchVideo(param).videoDocuments.orEmpty()
            )
            1 -> response.addAll(
                NetworkClient.searchNetwork.searchImage(param).imageDocuments.orEmpty()
            )
            2 -> response.addAll(
                NetworkClient.searchNetwork.searchVideo(param).videoDocuments.orEmpty()
            )
        }

        if (page > 1) {
            connectSearchResult(response)
        } else {
            registerSearchResult(response)
        }
    }

    // 서버로 보내는 요청 쿼리 설정하는 함수
    private fun setUpSearchParameter(word: String, page: String): HashMap<String, String> {
        return hashMapOf(
            "query" to word,
            "sort" to "accuracy",
            "page" to page,
        )
    }

    // 첫번째 검색 시 실행하는 함수
    fun processFirstSearch(word: String) {
        if (word == "") return

        page = 1
        updateLastWord(word)
        type.value?.let { communicationNetwork(word, page++, it) }
    }

    // 스크롤을 최하단으로 내렸을 시 실행하는 함수
    fun processScrollSearch() {
        type.value?.let { communicationNetwork(lastWord.value.toString(), page++, it) }
    }

    // 필터 타입을 정하는 함수
    fun setType(type: Int) {
        _type.value = type
    }

    // 필터 타입 값이 변했을 때 호출되는 함수
    fun filter(word: String, type: Int) {
        if (word == "" || searchUiState.value?.searchResult.isNullOrEmpty()) return

        page = 1
        communicationNetwork(lastWord.value.toString(), page++, type)
    }
}