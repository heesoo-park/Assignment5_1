package com.example.searchcollectapp.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.searchcollectapp.R
import com.example.searchcollectapp.databinding.FragmentBottomSheetBinding
import com.example.searchcollectapp.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchBottomSheet: BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    // 공유 뷰모델 선언
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgBottomSheetClearBtn.setOnClickListener {
            dismiss()
        }

        binding.toggleGroupFilterType.check(
            when (viewModel.type.value) {
                0 -> R.id.btn_type_1
                1 -> R.id.btn_type_2
                else -> R.id.btn_type_3
            }
        )

        binding.toggleGroupFilterType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_type_1 -> {
                        viewModel.setType(0)
                    }
                    R.id.btn_type_2 -> {
                        viewModel.setType(1)
                    }
                    R.id.btn_type_3 -> {
                        viewModel.setType(2)
                    }
                }
            }
        }
    }
}