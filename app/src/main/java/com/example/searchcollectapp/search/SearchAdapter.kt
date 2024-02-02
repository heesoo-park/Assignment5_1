package com.example.searchcollectapp.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchcollectapp.Document
import com.example.searchcollectapp.UseConstant.inputFormat
import com.example.searchcollectapp.UseConstant.outputFormat
import com.example.searchcollectapp.databinding.SearchResultImageBinding
import com.example.searchcollectapp.databinding.SearchResultVideoBinding
import java.util.Date

class SearchAdapter(private val context: Context) :
    ListAdapter<Document, RecyclerView.ViewHolder>(ListComparator()) {

    // 아이템에서 썸네일 클릭 이벤트 처리할 인테페이스 선언
    interface SearchThumbnailClickListener {
        fun onClick(selectedDocument: Document)
        fun onLongClick(selectedDocument: Document)
    }

    var searchThumbnailClickListener: SearchThumbnailClickListener? = null

    // 이미지 데이터 처리하는 뷰 홀더
    inner class ImageSearchViewHolder(private val binding: SearchResultImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document.ImageDocument) {
            binding.apply {
                Glide.with(context)
                    .load(document.thumbnailUrl)
                    .into(ivSearchResultImageThumbnail)
                tvSearchResultImageSite.text = document.displaySiteName
                tvSearchResultImageDatetime.text =
                    outputFormat.format(inputFormat.parse(document.dateTime) as Date)
                ivSearchResultImageFavorite.isVisible = document.isSelected
                ivSearchResultImageThumbnail.setOnClickListener {
                    searchThumbnailClickListener?.onClick(document)
                }
                ivSearchResultImageThumbnail.setOnLongClickListener {
                    searchThumbnailClickListener?.onLongClick(document)
                    true
                }
            }
        }
    }

    // 비디오 데이터 처리하는 뷰 홀더
    inner class VideoSearchViewHolder(private val binding: SearchResultVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document.VideoDocument) {
            binding.apply {
                Glide.with(context)
                    .load(document.thumbnailUrl)
                    .into(ivSearchResultVideoThumbnail)
                tvSearchResultVideoTitle.text = document.title
                tvSearchResultVideoDatetime.text =
                    outputFormat.format(inputFormat.parse(document.dateTime) as Date)
                ivSearchResultVideoFavorite.isVisible = document.isSelected
                ivSearchResultVideoThumbnail.setOnClickListener {
                    searchThumbnailClickListener?.onClick(document)
                }
                ivSearchResultVideoThumbnail.setOnLongClickListener {
                    searchThumbnailClickListener?.onLongClick(document)
                    true
                }
            }
        }
    }

    // 뷰 타입에 따라 다르게 뷰홀더를 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                ImageSearchViewHolder(
                    SearchResultImageBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                VideoSearchViewHolder(
                    SearchResultVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is Document.ImageDocument -> 0
            is Document.VideoDocument -> 1
        }
    }

    // 데이터 클래스에 따라 다르게 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (currentList[position]) {
            is Document.ImageDocument -> (holder as ImageSearchViewHolder).bind(currentList[position] as Document.ImageDocument)
            is Document.VideoDocument -> (holder as VideoSearchViewHolder).bind(currentList[position] as Document.VideoDocument)
        }
    }


    class ListComparator : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return if (oldItem is Document.ImageDocument && newItem is Document.ImageDocument) {
                oldItem.thumbnailUrl == newItem.thumbnailUrl
            } else if (oldItem is Document.VideoDocument && newItem is Document.VideoDocument) {
                oldItem.url == newItem.url
            } else {
                false
            }
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }
    }
}