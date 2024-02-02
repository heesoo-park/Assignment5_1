package com.example.searchcollectapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.searchcollectapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 검색 결과를 저장하는 리스트
    private var items: ArrayList<Document> = arrayListOf()
    // 선택한 데이터를 저장하는 리스트
    private var favoriteItems: ArrayList<Document> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // SharedPreferences 설정
        val pref = getSharedPreferences("pref",0)
        // 마지막 검색 단어 저장된 값 불러오기
        binding.etMainSearch.setText(pref.getString("searchWord",""))

        // 검색 버튼 클릭 이벤트
        binding.btnMainSearch.setOnClickListener {
            val word = binding.etMainSearch.text.toString()
            if (word.isNotEmpty()) {
                saveLastSearchWord(word)
                communicationNetwork(setUpSearchParameter(word))
                Toast.makeText(this, "검색 중입니다...", Toast.LENGTH_SHORT).show()
            }

            binding.etMainSearch.clearFocus()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(window.decorView.applicationWindowToken, 0)
        }

        // 검색결과 버튼 클릭 이벤트
        binding.btnMainSearchImage.setOnClickListener {
            setFragment(SearchFragment.newInstance(items, favoriteItems))
        }

        // 내 보관함 버튼 클릭 이벤트
        binding.btnMainStorageBox.setOnClickListener {
            setFragment(StorageFragment.newInstance(favoriteItems))
        }
    }

    // 서버로부터 데이터 받아오는 함수
    private fun communicationNetwork(param: HashMap<String, String>) = lifecycleScope.launch {
        val responseData = NetworkClient.searchNetwork.searchImage(param)
        items.clear()
        responseData.documents?.forEach {
            items.add(it)
        }
        items.sortByDescending { it.dateTime }

        setFragment(SearchFragment.newInstance(items, favoriteItems))
    }

    // 요청 파라미터 세팅하는 함수
    private fun setUpSearchParameter(word: String): HashMap<String, String> {
        return hashMapOf(
            "query" to word,
            "sort" to "accuracy",
            "page" to "1",
            "size" to "80"
        )
    }

    // 프래그먼트 전환해주는 함수
    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.frame_layout_main, fragment)
            setReorderingAllowed(true)
        }
    }

    private fun saveLastSearchWord(word: String) {
        val pref = getSharedPreferences("pref", MODE_PRIVATE)
        val edit = pref.edit()
        edit.putString("searchWord", word)
        edit.apply()
    }
}
