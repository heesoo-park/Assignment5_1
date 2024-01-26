package com.example.searchcollectapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.searchcollectapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), SendFavoriteInfo {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

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

        binding.btnMainSearchImage.setOnClickListener {
            setFragment(SearchFragment.newInstance(items, favoriteItems))
        }

        binding.btnMainStorageBox.setOnClickListener {
            setFragment(StorageFragment.newInstance(favoriteItems))
        }
    }

    private fun communicationNetwork(param: HashMap<String, String>) = lifecycleScope.launch {
        val responseData = NetworkClient.searchNetwork.searchImage(param)
        items.clear()
        responseData.documents?.forEach {
            items.add(it)
        }
        items.sortByDescending { it.dateTime }

        setFragment(SearchFragment.newInstance(items, favoriteItems))
    }

    private fun setUpSearchParameter(word: String): HashMap<String, String> {
        return hashMapOf(
            "query" to word,
            "sort" to "accuracy",
            "page" to "1",
            "size" to "80"
        )
    }


    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.frame_layout_main, fragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

    override fun sendDocument(document: Document) {
        if (favoriteItems.find { it == document } != null) {
            favoriteItems.removeIf { it == document }
        } else {
            favoriteItems.add(document)
        }
        favoriteItems.sortByDescending { it.dateTime }
    }

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
