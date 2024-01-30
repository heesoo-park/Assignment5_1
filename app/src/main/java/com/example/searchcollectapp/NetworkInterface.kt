package com.example.searchcollectapp

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface NetworkInterface {
    // 이미지를 가져오는 HTTP 메서드
    @Headers("Authorization: KakaoAK 56f3649492b8c6cbcba10fdf9e3025ec")
    @GET("v2/search/image")
    suspend fun searchImage(@QueryMap param: HashMap<String, String>) : ImageSearchResponse

    // 비디오를 가져오는 HTTP 메서드
    @Headers("Authorization: KakaoAK 56f3649492b8c6cbcba10fdf9e3025ec")
    @GET("v2/search/vclip")
    suspend fun searchVideo(@QueryMap param: HashMap<String, String>) : VideoSearchResponse
}