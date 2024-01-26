package com.example.searchcollectapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchcollectapp.databinding.FragmentSearchBinding

interface SendFavoriteInfo {
    fun sendDocument(document: Document)
}

class SearchFragment : Fragment() {

    companion object {
        fun newInstance(result: ArrayList<Document>, favoriteItems: ArrayList<Document>) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("result", result)
                    putParcelableArrayList("favorite", favoriteItems)
                }
            }
    }

    private var sendFavoriteInfo: SendFavoriteInfo? = null

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var result: ArrayList<Document> = arrayListOf()
    private var favoriteItems: ArrayList<Document> = arrayListOf()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sendFavoriteInfo = context as SendFavoriteInfo
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getParcelableArrayList("result", Document::class.java) as ArrayList<Document>
            favoriteItems = it.getParcelableArrayList("favorite", Document::class.java) as ArrayList<Document>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = SearchAdapter(requireContext(), result, favoriteItems)
        adapter.searchThumbnailClickListener = object : SearchAdapter.SearchThumbnailClickListener {
            override fun onClick(view: View, position: Int) {
                if (favoriteItems.find { it == result[position] } != null) {
                    favoriteItems.removeIf { it == result[position] }
                } else {
                    favoriteItems.add(result[position])
                }
                favoriteItems.sortByDescending { it.dateTime }

                view.isVisible = !view.isVisible
            }
        }
        binding.rvSearchList.adapter = adapter
        binding.rvSearchList.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}