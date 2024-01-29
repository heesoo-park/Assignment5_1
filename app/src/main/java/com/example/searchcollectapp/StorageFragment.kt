package com.example.searchcollectapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchcollectapp.databinding.FragmentStorageBinding

class StorageFragment : Fragment() {

    private var _binding: FragmentStorageBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    val adapter: StorageAdapter by lazy {
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

        initViewModel()

        adapter.storageThumbnailClickListener = object : StorageAdapter.StorageThumbnailClickListener {
            override fun onClick(selectedDocument: Document) {
                viewModel.changeMarker(selectedDocument)
                viewModel.registerFavoriteResult(selectedDocument)
            }
        }

        binding.rvStorageList.adapter = adapter
        binding.rvStorageList.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun initViewModel() = with(viewModel) {
        favoriteResult.observe(viewLifecycleOwner) { uiState ->
            adapter.removeItem(uiState.favoriteResult.toList())
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}