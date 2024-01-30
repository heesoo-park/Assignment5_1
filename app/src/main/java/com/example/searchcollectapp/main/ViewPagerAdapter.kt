package com.example.searchcollectapp.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.searchcollectapp.search.SearchFragment
import com.example.searchcollectapp.storage.StorageFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    // 사용되는 프래그먼트를 리스트 형태로 저장
    private val fragments = listOf(
        SearchFragment(),
        StorageFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment = fragments[position]
}