package com.example.fitnesstracker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.fitnesstracker.data.AppModule
import com.example.fitnesstracker.data.WorkoutRepository
import com.example.fitnesstracker.ui.theme.FitnesstrackerTheme
import com.example.fitnesstracker.ui.theme.Navigation.BottomNavItem
import com.example.fitnesstracker.ui.theme.Navigation.NavigationGraph
import com.example.fitnesstracker.utils.RealTimeStepTracker
import com.example.fitnesstracker.viewmodel.StepViewModel
import com.example.fitnesstracker.viewmodel.StepViewModelFactory
import com.example.fitnesstracker.viewmodel.WorkoutViewModel
import com.example.fitnesstracker.viewmodel.WorkoutViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppModule.provideDatabase(applicationContext)
        val repository = WorkoutRepository(database.workoutDao(), database.stepDao())

        setContent {
            // Get the StepViewModel once at the top level so its state can be shared.
            val stepViewModel: StepViewModel = viewModel(factory = StepViewModelFactory(repository))

            // Initialize user preferences as soon as the app starts.
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                stepViewModel.initUserPrefs(context)
            }

            // Collect the theme state here to control the entire app's theme.
            val isDarkTheme by stepViewModel.isDarkTheme.collectAsState()

            // ⭐️ FIX: The entire UI is now wrapped in FitnesstrackerTheme, which dynamically
            // receives the darkTheme parameter from our ViewModel's state.
            FitnesstrackerTheme(darkTheme = isDarkTheme) {

                // --- PERMISSION HANDLING (Remains the same) ---
                var hasPermission by remember {
                    mutableStateOf(
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED
                    )
                }
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            hasPermission = true
                            RealTimeStepTracker.startTracking(applicationContext)
                        } else {
                            hasPermission = false
                        }
                    }
                )
                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (!hasPermission) {
                            permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                        } else {
                            RealTimeStepTracker.startTracking(applicationContext)
                        }
                    } else {
                        hasPermission = true
                        RealTimeStepTracker.startTracking(applicationContext)
                    }
                }

                // --- UI and Navigation ---
                val navController = rememberNavController()
                val workoutViewModel: WorkoutViewModel = viewModel(factory = WorkoutViewModelFactory(repository))
                val items = listOf(BottomNavItem.Home, BottomNavItem.Stopwatch, BottomNavItem.VideoCall, BottomNavItem.Profile)

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
                                            popUpTo(navController.graph.startDestinationId) { saveState = true }
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
                    Surface(modifier = Modifier.fillMaxSize().padding(padding)) {
                        // Pass the already-created ViewModel instances to the NavGraph.
                        NavigationGraph(
                            navController = navController,
                            workoutViewModel = workoutViewModel,
                            stepViewModel = stepViewModel
                        )
                    }
                }
            }
        }
    }
}