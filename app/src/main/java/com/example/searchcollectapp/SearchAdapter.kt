package com.example.searchcollectapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.searchcollectapp.databinding.SearchResultItemBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchAdapter(private val context: Context, private val results: MutableList<Document>, private val favoriteItems: MutableList<Document>) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    interface SearchThumbnailClickListener {
        fun onClick(view: View, position: Int)
    }

    var searchThumbnailClickListener: SearchThumbnailClickListener? = null

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    inner class SearchViewHolder(private val binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.ivSearchResultThumbnail
        val site = binding.tvSearchResultSite
        val datetime = binding.tvSearchResultDatetime
        val favorite = binding.ivSearchResultFavorite
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
        return results.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.thumbnail.setOnClickListener {
            searchThumbnailClickListener?.onClick(holder.favorite, position)
        }

        Glide.with(context)
            .load(results[position].thumbnailUrl)
            .into(holder.thumbnail)
        holder.site.text = results[position].displaySiteName
        holder.datetime.text =
            outputFormat.format(inputFormat.parse(results[position].dateTime) as Date)
        holder.favorite.isVisible = (favoriteItems.find { it == results[position] } != null)
    }
}