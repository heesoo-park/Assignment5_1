package com.example.searchcollectapp.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(context.getSharedPreferences("pref", Context.MODE_PRIVATE), context.getSharedPreferences("favorite", Context.MODE_PRIVATE)) as T
        }
        throw IllegalArgumentException("Unknown viewModel Class")
    }
}