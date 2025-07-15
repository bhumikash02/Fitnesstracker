package com.example.fitnesstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StopwatchViewModel : ViewModel() {

    private val _timeMillis = MutableStateFlow(0L)
    val timeMillis: StateFlow<Long> = _timeMillis

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _laps = MutableStateFlow<List<Long>>(emptyList())
    val laps: StateFlow<List<Long>> = _laps

    private var timerJob: Job? = null

    fun start() {
        if (_isRunning.value) return // Don't start if already running

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - _timeMillis.value
            while (_isRunning.value) {
                _timeMillis.value = System.currentTimeMillis() - startTime
                delay(10L) // Update every 10 milliseconds for smooth UI
            }
        }
    }

    fun pause() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun reset() {
        pause() // Stop the timer first
        _timeMillis.value = 0L
        _laps.value = emptyList()
    }

    fun lap() {
        if (!_isRunning.value) return
        val currentLaps = _laps.value.toMutableList()
        currentLaps.add(_timeMillis.value)
        _laps.value = currentLaps
    }
}

/**
 * A helper utility to format milliseconds into a displayable string (MM:SS:ss).
 */
fun formatTime(timeMillis: Long): String {
    val totalSeconds = timeMillis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val milliseconds = (timeMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
}