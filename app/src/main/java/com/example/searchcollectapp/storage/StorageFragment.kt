package com.example.searchcollectapp.storage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchcollectapp.Document
import com.example.searchcollectapp.databinding.FragmentStorageBinding
import com.example.searchcollectapp.main.MainViewModel

class StorageFragment : Fragment() {

    // 뷰 바인딩 초기화
    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    // 공유 뷰모델 선언
    private val viewModel: MainViewModel by activityViewModels()

    // 어댑터 지연 초기화
    private val storageAdapter: StorageAdapter by lazy {
        StorageAdapter(requireContext())
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

        initView()
        initViewModel()
    }

    // 뷰 초기화하는 함수
    private fun initView() {
        // 리사이클러뷰 아이템 클릭 이벤트
        storageAdapter.storageThumbnailClickListener = object :
            StorageAdapter.StorageThumbnailClickListener {
            override fun onClick(selectedImageDocument: Document) {
                viewModel.controlMarker(selectedImageDocument)
                viewModel.manageSelectedDocument(selectedImageDocument)
            }
        }

        binding.rvStorageList.adapter = storageAdapter
        binding.rvStorageList.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    // 뷰모델 초기화하는 함수
    private fun initViewModel() = with(viewModel) {
        storageUiState.observe(viewLifecycleOwner) { uiState ->
            storageAdapter.submitList(uiState.selectedResult.toList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}