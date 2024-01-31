package com.example.searchcollectapp.storage

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchcollectapp.Document
import com.example.searchcollectapp.UseConstant.inputFormat
import com.example.searchcollectapp.UseConstant.outputFormat
import com.example.searchcollectapp.databinding.StorageResultImageBinding
import com.example.searchcollectapp.databinding.StorageResultVideoBinding
import java.util.Date

class StorageAdapter(private val context: Context) :
    ListAdapter<Document, RecyclerView.ViewHolder>(ListComparator()){

    // 아이템에서 썸네일 클릭 이벤트 처리할 인터페이스 선언
    interface StorageThumbnailClickListener {
        fun onClick(selectedImageDocument: Document)
    }

    var storageThumbnailClickListener: StorageThumbnailClickListener? = null

    // 이미지 데이터 처리하는 뷰 홀더
    inner class ImageStorageViewHolder(private val binding: StorageResultImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document.ImageDocument) {
            binding.apply {
                Glide.with(context)
                    .load(document.thumbnailUrl)
                    .into(ivStorageResultImageThumbnail)
                tvStorageResultImageSite.text = document.displaySiteName
                tvStorageResultImageDatetime.text =
                    outputFormat.format(inputFormat.parse(document.dateTime) as Date)
                ivStorageResultImageThumbnail.setOnClickListener {
                    storageThumbnailClickListener?.onClick(document)
                }
            }
        }
    }

    // 비디오 데이터 처리하는 뷰 홀더
    inner class VideoStorageViewHolder(private val binding: StorageResultVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(document: Document.VideoDocument) {
            binding.apply {
                Glide.with(context)
                    .load(document.thumbnailUrl)
                    .into(ivStorageResultVideoThumbnail)
                tvStorageResultVideoTitle.text = document.title
                tvStorageResultVideoDatetime.text =
                    outputFormat.format(inputFormat.parse(document.dateTime) as Date)
                ivStorageResultVideoThumbnail.setOnClickListener {
                    storageThumbnailClickListener?.onClick(document)
                }
            }
        }
    }

    // 뷰 타입에 따라 다르게 뷰홀더를 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                ImageStorageViewHolder(StorageResultImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
            else -> {
                VideoStorageViewHolder(StorageResultVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(currentList[position]) {
            is Document.ImageDocument -> 0
            is Document.VideoDocument -> 1
        }
    }

    // 데이터 클래스에 따라 다르게 바인딩
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (currentList[position]) {
            is Document.ImageDocument -> (holder as ImageStorageViewHolder).bind(currentList[position] as Document.ImageDocument)
            is Document.VideoDocument -> (holder as VideoStorageViewHolder).bind(currentList[position] as Document.VideoDocument)
        }
    }

    class ListComparator : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }
    }
}