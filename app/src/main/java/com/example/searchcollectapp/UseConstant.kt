package com.example.searchcollectapp

import java.text.SimpleDateFormat
import java.util.Locale

object UseConstant {
    const val SP_PREF = "shared_preferences_pref"
    const val SP_SELECT = "shared_preferences_select"

    const val KEY_SEARCH_WORD = "key_search_word"
    const val KEY_SELECT_DATASET = "key_select_dataset"

    const val MEMBER_NAME = "image_url"

    // 날짜와 시간 처리를 위한 포맷 변수들
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
}