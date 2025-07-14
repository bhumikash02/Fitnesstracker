package com.example.fitnesstracker.utils

import android.content.Context
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.fitnesstracker.model.StepData
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.Locale

class HealthConnectManager(private val context: Context) {

    private val healthConnectClient: HealthConnectClient?

    val permissions = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getWritePermission(StepsRecord::class)
    )

    init {
        healthConnectClient = try {
            HealthConnectClient.getOrCreate(context)
        } catch (e: Exception) {
            Log.e("HealthConnectManager", "Health Connect provider not available.", e)
            null
        }
    }

    fun isApiAvailable(): Boolean = healthConnectClient != null

    suspend fun hasAllPermissions(): Boolean {
        return if (isApiAvailable()) {
            val granted = healthConnectClient!!.permissionController.getGrantedPermissions()
            granted.containsAll(permissions)
        } else {
            false
        }
    }

    fun requestPermissionsActivityContract(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    suspend fun readTodayStepCount(): Int {
        if (!isApiAvailable() || !hasAllPermissions()) return 0

        val startOfDay = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)
        val now = Instant.now()
        val timeRangeFilter = TimeRangeFilter.between(startOfDay.toInstant(), now)
        val request = ReadRecordsRequest(StepsRecord::class, timeRangeFilter)

        return try {
            val response = healthConnectClient!!.readRecords(request)
            response.records.sumOf { it.count.toInt() }
        } catch (e: Exception) {
            Log.e("HealthConnectManager", "Could not read today's steps", e)
            0
        }
    }

    suspend fun readWeeklySteps(): List<StepData> {
        if (!isApiAvailable() || !hasAllPermissions()) return emptyList()

        val weeklyData = mutableListOf<StepData>()
        val today = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS)

        for (i in 6 downTo 0) {
            val day = today.minusDays(i.toLong())
            val startOfDay = day.toInstant()
            val endOfDay = day.plusDays(1).toInstant().minusNanos(1)

            val request = ReadRecordsRequest(
                StepsRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startOfDay, endOfDay)
            )
            try {
                val response = healthConnectClient!!.readRecords(request)
                val totalSteps = response.records.sumOf { it.count.toInt() }
                val dayAbbreviation = day.dayOfWeek.name
                    .take(3)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                weeklyData.add(StepData(dayAbbreviation, totalSteps))
            } catch (e: Exception) {
                Log.e("HealthConnectManager", "Could not read steps for $day", e)
                val dayAbbreviation = day.dayOfWeek.name
                    .take(3)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                weeklyData.add(StepData(dayAbbreviation, 0))
            }
        }
        return weeklyData
    }
}