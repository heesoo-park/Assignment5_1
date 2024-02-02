package com.example.searchcollectapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.searchcollectapp.R
import com.example.searchcollectapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // 뷰바인딩 변수 지연초기화
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 뒤로가기 버튼에 관련된 콜백 함수 초기화
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // 현재 검색 결과 페이지에 있을 때
            if (binding.viewPagerMain.currentItem == 0) {
                finish()
            } else { // 현재 내 보관함 페이지에 있을 때
                // 검색 결과 페이지로 이동
                binding.viewPagerMain.currentItem = 0
                binding.bottomNavigationViewMain.menu.getItem(0).isChecked = true
            }
        }
    }

    // 뷰페이저 어댑터 초기화
    private val viewPagerAdapter = ViewPagerAdapter(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBackPressCallback()
        initView()
    }

    // 뷰 초기화하는 함수
    private fun initView() = with(binding) {
        viewPagerMain.adapter = viewPagerAdapter

        // 뷰페이저와 바텀내비게이션 연동
        viewPagerMain.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationViewMain.menu.getItem(position).isChecked = true
            }
        })

        // 뷰페이저와 바텀내비게이션 연동
        bottomNavigationViewMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_search -> {
                    viewPagerMain.currentItem = 0
                    return@setOnItemSelectedListener true
                }

                R.id.menu_storage -> {
                    viewPagerMain.currentItem = 1
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false
            }
        }
    }

    // 위에서 선언한 콜백 함수 onBackPressedDispatcher에 등록하는 함수
    private fun setBackPressCallback() {
        this.onBackPressedDispatcher.addCallback(this, callback)
    }
}
