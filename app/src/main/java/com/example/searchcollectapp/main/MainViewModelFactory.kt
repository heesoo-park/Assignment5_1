package com.example.searchcollectapp.main

import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.searchcollectapp.UseConstant.SP_PREF
import com.example.searchcollectapp.UseConstant.SP_SELECT

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                context.getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager,
                context.getSharedPreferences(SP_PREF, Context.MODE_PRIVATE),
                context.getSharedPreferences(SP_SELECT, Context.MODE_PRIVATE)
            ) as T
        }
        throw IllegalArgumentException("Unknown viewModel Class")
    }
}