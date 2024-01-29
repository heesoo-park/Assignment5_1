package com.example.searchcollectapp

import android.app.ProgressDialog.show
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.viewpager2.widget.ViewPager2
import com.example.searchcollectapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.viewPagerMain.currentItem == 0) {
                val pref = getSharedPreferences("pref", MODE_PRIVATE)
                val edit = pref.edit()
                edit.putString("searchWord", adapter.getLastWord())
                edit.apply()
                finish()
            } else {
                binding.viewPagerMain.currentItem = 0
                binding.bottomNavigationViewMain.menu.getItem(0).isChecked = true
            }
        }
    }

    private val adapter = ViewPagerAdapter(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        this.onBackPressedDispatcher.addCallback(this, callback)

        binding.viewPagerMain.adapter = adapter

        binding.viewPagerMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigationViewMain.menu.getItem(position).isChecked = true
            }
        })

        binding.bottomNavigationViewMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_search -> {
                    binding.viewPagerMain.currentItem = 0
                    return@setOnItemSelectedListener true
                }
                R.id.menu_storage -> {
                    binding.viewPagerMain.currentItem = 1
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }
}
