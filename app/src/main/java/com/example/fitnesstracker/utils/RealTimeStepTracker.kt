package com.example.fitnesstracker.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

// Ensure this is an 'object' so it's a singleton
object RealTimeStepTracker : SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null

    // This is the variable we are trying to access
    private val _totalSteps = MutableLiveData(0)
    val totalSteps: LiveData<Int> = _totalSteps // Expose it as a public, unmodifiable LiveData

    fun startTracking(context: Context) {
        Log.d("StepDebug", "Attempting to start RealTimeStepTracker...")
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Log.e("StepDebug", "FATAL: No step counter sensor (TYPE_STEP_COUNTER) found on this device.")
            return
        }

        val success = sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        if (success) {
            Log.d("StepDebug", "SUCCESS: Sensor listener registered successfully for TYPE_STEP_COUNTER.")
        } else {
            Log.e("StepDebug", "FAILURE: Sensor listener registration failed.")
        }
    }

    fun stopTracking() {
        if (::sensorManager.isInitialized) {
            sensorManager.unregisterListener(this)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val count = event.values[0].toInt()
            if (_totalSteps.value != count) {
                Log.d("StepDebug", "SENSOR FIRED: New total steps from hardware: $count")
                _totalSteps.value = count
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this implementation
    }
}