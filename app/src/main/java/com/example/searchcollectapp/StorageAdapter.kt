package com.example.searchcollectapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchcollectapp.databinding.SearchResultItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StorageAdapter(private val context: Context, private val favoriteItems: MutableList<Document>) :
    RecyclerView.Adapter<StorageAdapter.StorageViewHolder>() {

    // 아이템 썸네일 클릭 했을 때 처리하기 위한 인터페이스
    interface StorageThumbnailClickListener {
        fun onClick(position: Int)
    }

    var storageThumbnailClickListener: StorageThumbnailClickListener? = null

    // 날짜 형식을 포맷하기 위한 변수들
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    inner class StorageViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.ivSearchResultThumbnail
        val site = binding.tvSearchResultSite
        val datetime = binding.tvSearchResultDatetime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageViewHolder {
        return StorageViewHolder(
            SearchResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return favoriteItems.size
    }

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        holder.thumbnail.setOnClickListener {
            storageThumbnailClickListener?.onClick(position)
        }

        Glide.with(context)
            .load(favoriteItems[position].thumbnailUrl)
            .into(holder.thumbnail)
        holder.site.text = favoriteItems[position].displaySiteName
        holder.datetime.text =
            outputFormat.format(inputFormat.parse(favoriteItems[position].dateTime) as Date)
    }
}