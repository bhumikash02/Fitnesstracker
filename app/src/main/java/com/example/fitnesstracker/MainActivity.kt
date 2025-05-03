package com.example.fitnesstracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.example.fitnesstracker.data.AppModule
import com.example.fitnesstracker.data.WorkoutRepository
import com.example.fitnesstracker.ui.theme.FitnesstrackerTheme
import com.example.fitnesstracker.ui.theme.Navigation.BottomNavItem
import com.example.fitnesstracker.ui.theme.Navigation.NavigationGraph
import com.example.fitnesstracker.viewmodel.StepViewModel
import com.example.fitnesstracker.viewmodel.StepViewModelFactory
import com.example.fitnesstracker.viewmodel.WorkoutViewModel
import com.example.fitnesstracker.viewmodel.WorkoutViewModelFactory
import java.util.concurrent.TimeUnit
import com.example.fitnesstracker.worker.SyncStepsWorker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize database and repository
        val database = AppModule.provideDatabase(applicationContext)
        val repository = WorkoutRepository(database.workoutDao())

        setContent {
            FitnesstrackerTheme {
                val navController = rememberNavController()

                val workoutViewModel: WorkoutViewModel = viewModel(
                    factory = WorkoutViewModelFactory(repository)
                )
                val stepViewModel: StepViewModel = viewModel(
                    factory = StepViewModelFactory(repository)
                )


                val items = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Stopwatch,
                    BottomNavItem.VideoCall,
                    BottomNavItem.Profile
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentRoute = navBackStackEntry?.destination?.route
                            items.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = {
                                        navController.navigate(item.route) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavigationGraph(
                            navController = navController,
                            workoutViewModel = workoutViewModel,
                            stepViewModel = stepViewModel
                        )
                    }
                }
            }
        }

        // 🔥 Start background step sync worker (Optional - for syncing steps even when app closed)
        setupBackgroundStepSync()
    }

    private fun setupBackgroundStepSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncStepsWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "syncStepsWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
