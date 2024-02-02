package com.example.searchcollectapp.search

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.searchcollectapp.Document
import com.example.searchcollectapp.R
import com.example.searchcollectapp.databinding.FragmentSearchBinding
import com.example.searchcollectapp.main.MainViewModel
import com.example.searchcollectapp.main.MainViewModelFactory

class SearchFragment : Fragment() {

    // 뷰 바인딩 초기화
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    // 공유 뷰모델 선언
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(requireContext())
    }

    // 어댑터 지연 초기화
    private val searchAdapter by lazy {
        SearchAdapter(requireContext())
    }

    // 페이드인 애니메이션
    private val fadeIn: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
    }

    // 페이드아웃 애니메이션
    private val fadeOut: Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
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

        initView()
        initViewModel()
    }

    // 뷰 초기화하는 함수
    private fun initView() = with(binding) {
        viewModel.loadWordSharedPreferences()
        viewModel.loadDataSharedPreferences()

        // 검색 버튼 클릭 이벤트
        btnSearch.setOnClickListener {
            viewModel.processFirstSearch(etSearch.text.toString())
            viewModel.saveWordSharedPreferences()
            viewModel.goToFirstState()
        }

        // 키보드 검색 버튼 클릭 이벤트
        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.processFirstSearch(etSearch.text.toString())
                viewModel.saveWordSharedPreferences()
                viewModel.goToFirstState()
            }

            false
        }

        // 리사이클러뷰 아이템 클릭 이벤트, 롱클릭 이벤트
        searchAdapter.searchThumbnailClickListener =
            object : SearchAdapter.SearchThumbnailClickListener {
                override fun onClick(selectedDocument: Document) {
                    viewModel.controlMarker(selectedDocument)
                    viewModel.manageSelectedDocument(selectedDocument)
                }

                override fun onLongClick(selectedDocument: Document) {
                    viewModel.storeClipboard(selectedDocument)
                    Toast.makeText(
                        requireContext(),
                        "해당 데이터의 URL이 클립보드에 저장되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        rvSearchList.adapter = searchAdapter
        rvSearchList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            }
        rvSearchList.itemAnimator = null

        // 리사이클러뷰 스크롤에 따라 나타나고 사라지는 플로팅 액션 버튼 처리
        rvSearchList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var curVisible = false
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.canScrollVertically(-1)
                        .not()
                ) {
                    fabSearchScrollUp.startAnimation(fadeOut)
                    fabSearchScrollUp.isVisible = false
                    curVisible = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    fabSearchScrollUp.isVisible = true
                    if (curVisible.not()) fabSearchScrollUp.startAnimation(fadeIn)
                    curVisible = true
                }


                if (recyclerView.canScrollVertically(1).not()) {
                    viewModel.processScrollSearch()
                }
            }
        })

        // 플로팅 액션 버튼 클릭 이벤트
        fabSearchScrollUp.setOnClickListener {
            viewModel.goToFirstState()
        }

        // 필터 버튼 클릭 이벤트
        btnSearchFilter.setOnClickListener {
            val bottomSheet = SearchBottomSheet()
            bottomSheet.show(requireActivity().supportFragmentManager, bottomSheet.tag)
        }
    }

    // 뷰모델 초기화하는 함수
    private fun initViewModel() = with(viewModel) {
        searchUiState.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it.searchResult.toMutableList())
        }

        lastWord.observe(viewLifecycleOwner) {
            binding.etSearch.setText(it)
        }

        type.observe(viewLifecycleOwner) {
            viewModel.filter(binding.etSearch.text.toString())
        }

        settingEvent.observe(viewLifecycleOwner) {
            binding.rvSearchList.smoothScrollToPosition(0)
            binding.etSearch.clearFocus()

            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                requireActivity().window.decorView.applicationWindowToken,
                0
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}