package com.example.fitnesstracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fitnesstracker.utils.GoogleFitManager
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SyncStepsWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            GoogleFitManager.readTodayStepCount(context, account) { steps ->
                GoogleFitManager.syncStepsToGoogleFit(context, account, steps)
            }
        }
        return Result.success()
    }
}
