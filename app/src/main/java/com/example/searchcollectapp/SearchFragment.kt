package com.example.searchcollectapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchcollectapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private var result: ArrayList<Document> = arrayListOf()
    private var favoriteItems: ArrayList<Document> = arrayListOf()

    private val adapter by lazy {
        SearchAdapter(
            requireContext(),
            viewModel.searchUiState.value?.searchResult.orEmpty().toMutableList(),
            viewModel.favoriteResult.value?.favoriteResult.orEmpty().toMutableList()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result =
                it.getParcelableArrayList("result", Document::class.java) as ArrayList<Document>
            favoriteItems =
                it.getParcelableArrayList("favorite", Document::class.java) as ArrayList<Document>
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

        initViewModel()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.searchUiState.observe(viewLifecycleOwner) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        adapter.searchThumbnailClickListener = object : SearchAdapter.SearchThumbnailClickListener {
            override fun onClick(view: View, position: Int) {
                viewModel.registerFavoriteResult(
                    viewModel.searchUiState.value?.searchResult?.get(
                        position
                    ) ?: Document("", "", "", "", 0, "", "", 0)
                )
                view.isVisible = !view.isVisible
            }
        }

        binding.rvSearchList.adapter = adapter
        binding.rvSearchList.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun initViewModel() = with(viewModel) {
        searchUiState.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}