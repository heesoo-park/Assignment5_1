package com.example.searchcollectapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchcollectapp.databinding.SearchResultItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StorageAdapter(private val context: Context) :
    ListAdapter<Document, StorageAdapter.StorageViewHolder>(ListComparator()){

    interface StorageThumbnailClickListener {
        fun onClick(selectedDocument: Document)
    }

    var storageThumbnailClickListener: StorageThumbnailClickListener? = null

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    inner class StorageViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(document: Document) {
            binding.apply {
                Glide.with(context)
                    .load(document.thumbnailUrl)
                    .into(ivSearchResultThumbnail)
                tvSearchResultSite.text = document.displaySiteName
                tvSearchResultDatetime.text =
                    outputFormat.format(inputFormat.parse(document.dateTime) as Date)
                ivSearchResultThumbnail.setOnClickListener {
                    storageThumbnailClickListener?.onClick(document)
                }
            }
        }
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
        return currentList.size
    }

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ListComparator : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }
    }

    fun removeItem(newList: List<Document>) {
        submitList(newList)
    }
}