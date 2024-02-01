package com.example.searchcollectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchcollectapp.databinding.FragmentStorageBinding

class StorageFragment : Fragment() {

    companion object {
        fun newInstance(favoriteItems: ArrayList<Document>) =
            StorageFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("favorite", favoriteItems)
                }
            }
    }

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    // 선택한 데이터를 저장하는 리스트
    private var favoriteItems: ArrayList<Document> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            favoriteItems = it.getParcelableArrayList("favorite", Document::class.java) as ArrayList<Document>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StorageAdapter(requireContext(), favoriteItems)
        // 리사이클러뷰 아이템 썸네일 클릭 리스너
        adapter.storageThumbnailClickListener = object : StorageAdapter.StorageThumbnailClickListener {
            override fun onClick(position: Int) {
                favoriteItems.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        }
        binding.rvStorageList.adapter = adapter
        binding.rvStorageList.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}