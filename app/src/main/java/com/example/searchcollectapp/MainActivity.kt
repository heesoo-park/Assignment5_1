package com.example.searchcollectapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.searchcollectapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()

    private var items: ArrayList<Document> = arrayListOf()
    private var favoriteItems: ArrayList<Document> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = getSharedPreferences("pref",0)
        binding.etMainSearch.setText(pref.getString("searchWord",""))

        binding.btnMainSearch.setOnClickListener {
            communicationNetwork(setUpSearchParameter(binding.etMainSearch.text.toString()))

            binding.etMainSearch.clearFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show()
        }

        val adapter = ViewPagerAdapter(this@MainActivity)
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

    private fun communicationNetwork(param: HashMap<String, String>) = lifecycleScope.launch {
        val responseData = NetworkClient.searchNetwork.searchImage(param)
        items.clear()
        responseData.documents?.forEach {
            items.add(it)
        }
        items.sortByDescending { it.dateTime }

        viewModel.registerSearchResult(items)
    }

    private fun setUpSearchParameter(word: String): HashMap<String, String> {
        return hashMapOf(
            "query" to word,
            "sort" to "accuracy",
            "page" to "1",
            "size" to "80"
        )
    }

//    override fun sendDocument(document: Document) {
//        if (favoriteItems.find { it == document } != null) {
//            favoriteItems.removeIf { it == document }
//        } else {
//            favoriteItems.add(document)
//        }
//        favoriteItems.sortByDescending { it.dateTime }
//    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments[0] is SearchFragment) {
            val pref = getSharedPreferences("pref", MODE_PRIVATE)
            val edit = pref.edit()
            edit.putString("searchWord", binding.etMainSearch.text.toString())
            edit.apply()
            finish()
        }

        super.onBackPressed()
    }
}
