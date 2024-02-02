package com.example.searchcollectapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.searchcollectapp.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        // 프래그먼트를 만들 때 두개의 ArrayList를 받아옴
        fun newInstance(result: ArrayList<Document>, favoriteItems: ArrayList<Document>) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("result", result)
                    putParcelableArrayList("favorite", favoriteItems)
                }
            }
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // 검색 결과를 저장하는 리스트
    private var result: ArrayList<Document> = arrayListOf()
    // 선택한 데이터를 저장하는 리스트
    private var favoriteItems: ArrayList<Document> = arrayListOf()

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
        // 리사이클러뷰 아이템 썸네일 클릭 리스너
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
        binding.rvSearchList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvSearchList.itemAnimator = null
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}