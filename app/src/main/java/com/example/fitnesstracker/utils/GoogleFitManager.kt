package com.example.fitnesstracker.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.fitnesstracker.model.StepData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.*
import com.google.android.gms.fitness.request.DataReadRequest
import java.util.concurrent.TimeUnit

object GoogleFitManager {

    private const val TAG = "GoogleFitManager"
    const val GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1001

    fun getFitnessOptions(): FitnessOptions {
        return FitnessOptions.builder()
            .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
            .build()
    }

    fun checkPermissions(
        context: Context,
        activity: Activity,
        onAuthorized: (GoogleSignInAccount) -> Unit
    ) {
        val fitnessOptions = getFitnessOptions()
        val account = GoogleSignIn.getAccountForExtension(context, fitnessOptions)

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                activity,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                account,
                fitnessOptions
            )
        } else {
            onAuthorized(account)
        }
    }

    fun readTodayStepCount(
        context: Context,
        account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context),
        onResult: (Int) -> Unit
    ) {
        if (account == null) {
            Log.e(TAG, "Google account is null")
            onResult(0)
            return
        }

        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(1)

        val request = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(context, account)
            .readData(request)
            .addOnSuccessListener { response ->
                val totalSteps = response.buckets
                    .flatMap { it.dataSets }
                    .flatMap { it.dataPoints }
                    .sumOf { it.getValue(Field.FIELD_STEPS).asInt() }

                onResult(totalSteps)
            }
            .addOnFailureListener {
                Log.e(TAG, "Step count read failed", it)
                onResult(0)
            }
    }

    fun readWeeklySteps(
        context: Context,
        account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(context),
        onResult: (List<StepData>) -> Unit
    ) {
        if (account == null) {
            Log.e(TAG, "Google account is null")
            onResult(emptyList())
            return
        }

        val endTime = System.currentTimeMillis()
        val startTime = endTime - TimeUnit.DAYS.toMillis(7)

        val request = DataReadRequest.Builder()
            .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
            .bucketByTime(1, TimeUnit.DAYS)
            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
            .build()

        Fitness.getHistoryClient(context, account)
            .readData(request)
            .addOnSuccessListener { response ->
                val weekly = response.buckets.mapIndexed { index, bucket ->
                    val steps = bucket.dataSets
                        .flatMap { it.dataPoints }
                        .sumOf { it.getValue(Field.FIELD_STEPS).asInt() }

                    StepData("Day ${index + 1}", steps)
                }
                onResult(weekly)
            }
            .addOnFailureListener {
                Log.e(TAG, "Weekly steps read failed", it)
                onResult(emptyList())
            }
    }

    fun syncStepsToGoogleFit(
        context: Context,
        account: GoogleSignInAccount,
        steps: Int,
        timestamp: Long = System.currentTimeMillis()
    ) {
        val dataSource = DataSource.Builder()
            .setAppPackageName(context.packageName)
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setType(DataSource.TYPE_RAW)
            .build()

        val dataPoint = DataPoint.builder(dataSource)
            .setField(Field.FIELD_STEPS, steps)
            .setTimeInterval(timestamp, timestamp, TimeUnit.MILLISECONDS)
            .build()

        val dataSet = DataSet.builder(dataSource)
            .add(dataPoint)
            .build()

        Fitness.getHistoryClient(context, account)
            .insertData(dataSet)
            .addOnSuccessListener {
                Log.d(TAG, "Successfully synced $steps steps to Google Fit!")
            }
            .addOnFailureListener {
                Log.e(TAG, "Failed to sync steps to Google Fit", it)
            }
    }
}
