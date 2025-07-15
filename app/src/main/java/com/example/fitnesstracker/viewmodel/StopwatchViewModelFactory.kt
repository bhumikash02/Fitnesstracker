package com.example.fitnesstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StopwatchViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StopwatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StopwatchViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}