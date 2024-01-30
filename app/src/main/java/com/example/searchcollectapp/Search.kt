package com.example.searchcollectapp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

// 서버에서 가져오는 이미지 데이터 담는 데이터 클래스
data class ImageSearchResponse(
    @SerializedName("documents")
    val imageDocuments: MutableList<Document.ImageDocument>?,
    @SerializedName("meta")
    val metaData: MetaData?
)

// 서버에서 가져오는 비디오 데이터 담는 데이터 클래스
data class VideoSearchResponse(
    @SerializedName("documents")
    val videoDocuments: MutableList<Document.VideoDocument>?,
    @SerializedName("meta")
    val metaData: MetaData?
)

// 메타 데이터가 담기는 데이터 클래스
data class MetaData(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)

// 이미지와 비디오 데이터 모두를 감싸는 Document
// 데이터를 객체로 넘길 수 있게 Parcelize 어노테이션 추가 & Parcelable 상속
sealed class Document {
    @Parcelize
    data class ImageDocument(
        @SerializedName("collection")
        val collection: String,
        @SerializedName("datetime")
        val dateTime: String,
        @SerializedName("display_sitename")
        val displaySiteName: String,
        @SerializedName("doc_url")
        val docUrl: String,
        @SerializedName("height")
        val height: Int,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("thumbnail_url")
        val thumbnailUrl: String,
        @SerializedName("width")
        val width: Int,
        var isSelected: Boolean = false
    ): Document(), Parcelable

    @Parcelize
    data class VideoDocument(
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("datetime")
        val dateTime: String,
        @SerializedName("play_time")
        val playTime: Int,
        @SerializedName("thumbnail")
        val thumbnailUrl: String,
        @SerializedName("author")
        val author: String,
        var isSelected: Boolean = false
    ): Document(), Parcelable
}

