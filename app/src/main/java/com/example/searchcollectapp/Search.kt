package com.example.searchcollectapp

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

data class SearchResponse(
    @SerializedName("documents")
    val documents: MutableList<Document>?,
    @SerializedName("meta")
    val metaData: MetaData?
)

data class MetaData(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("pageable_count")
    val pageableCount: Int,
    @SerializedName("is_end")
    val isEnd: Boolean
)

@Parcelize
data class Document(
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
): Parcelable