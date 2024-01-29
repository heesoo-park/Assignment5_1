package com.example.searchcollectapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchcollectapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    private val adapter by lazy {
        SearchAdapter(requireContext())
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

        val pref = requireContext().getSharedPreferences("pref", 0)
        binding.etSearch.setText(pref.getString("searchWord", ""))

        binding.btnSearch.setOnClickListener {
            communicationNetwork(setUpSearchParameter(binding.etSearch.text.toString()))

            binding.etSearch.clearFocus()
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().window.decorView.applicationWindowToken,
                0
            )
            Toast.makeText(requireActivity(), "click", Toast.LENGTH_SHORT).show()
        }

        adapter.searchThumbnailClickListener = object : SearchAdapter.SearchThumbnailClickListener {
            override fun onClick(selectedDocument: Document) {
                viewModel.changeMarker(selectedDocument)
                viewModel.registerFavoriteResult(selectedDocument)
            }
        }

        binding.rvSearchList.adapter = adapter
        binding.rvSearchList.layoutManager = GridLayoutManager(requireContext(), 2)
    }

    private fun initViewModel() = with(viewModel) {
        searchUiState.observe(viewLifecycleOwner) {
            adapter.submitList(it.searchResult.toMutableList())
        }
    }

    private fun communicationNetwork(param: HashMap<String, String>) = lifecycleScope.launch {
        val responseData = NetworkClient.searchNetwork.searchImage(param)
        viewModel.registerSearchResult(responseData.documents.orEmpty().toList().sortedByDescending { it.dateTime })
    }

    private fun setUpSearchParameter(word: String): HashMap<String, String> {
        return hashMapOf(
            "query" to word,
            "sort" to "accuracy",
            "page" to "1",
            "size" to "80"
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun sendLastWord(): String {
        return binding.etSearch.text.toString()
    }
}