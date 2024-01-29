package com.example.searchcollectapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity)  {

    private val searchFragment = SearchFragment()
    private val storageFragment = StorageFragment()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> searchFragment
            else -> storageFragment
        }
    }

    fun getLastWord(): String {
        return searchFragment.sendLastWord()
    }
}