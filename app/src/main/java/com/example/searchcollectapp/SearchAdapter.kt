package com.example.searchcollectapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchcollectapp.databinding.SearchResultItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchAdapter(private val context: Context) :
    ListAdapter<Document, SearchAdapter.SearchViewHolder>(ListComparator()) {

    interface SearchThumbnailClickListener {
        fun onClick(selectedDocument: Document)
    }

    var searchThumbnailClickListener: SearchThumbnailClickListener? = null

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    inner class SearchViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(document: Document) {
            binding.apply {
                Glide.with(context)
                    .load(document.thumbnailUrl)
                    .into(ivSearchResultThumbnail)
                tvSearchResultSite.text = document.displaySiteName
                tvSearchResultDatetime.text =
                    outputFormat.format(inputFormat.parse(document.dateTime) as Date)
                ivSearchResultFavorite.isVisible = document.isLiked
                ivSearchResultThumbnail.setOnClickListener {
                    searchThumbnailClickListener?.onClick(document)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
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

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ListComparator : DiffUtil.ItemCallback<Document>() {
        override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }
    }
}