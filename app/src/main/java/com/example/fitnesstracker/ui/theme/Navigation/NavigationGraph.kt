package com.example.fitnesstracker.ui.theme.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitnesstracker.ui.theme.screen.*
import com.example.fitnesstracker.viewmodel.StepViewModel
import com.example.fitnesstracker.viewmodel.WorkoutViewModel
import kotlinx.coroutines.launch

@Composable
fun NavigationGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    stepViewModel: StepViewModel,

    ) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {

        composable(Routes.LOGIN) {
            LoginScreen(onLoginClick = {
                navController.navigate(Routes.HOME)
            })
        }

        // ⭐️ THIS IS THE CORRECTED BLOCK ⭐️
        composable(Routes.HOME) {
            // We now pass the view models and the navController directly
            HomeScreen(
                navController = navController,
                stepViewModel = stepViewModel,
                workoutViewModel = workoutViewModel
            )
        }

        composable(Routes.WORKOUT) {
            WorkoutLogScreen(
                viewModel = workoutViewModel,
                onBmiClick = { navController.navigate(Routes.BMI) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) }
            )
        }

        composable(Routes.HISTORY) {
            WorkoutHistoryScreen(viewModel = workoutViewModel)
        }

        composable(Routes.BMI) {
            BmiCalculatorScreen()
        }

        composable(Routes.STEP_COUNTER) {
            StepCounterScreen(viewModel = stepViewModel)
        }

        composable(Routes.PROFILE) {
            val userProfile by stepViewModel.userProfile.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            ProfileScreen(
                userProfile = userProfile,
                onSaveProfile = { updatedProfile ->
                    coroutineScope.launch {
                        stepViewModel.saveUserProfile(updatedProfile)
                    }
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                isDarkTheme = false,
                onThemeToggle = { }
            )
        }

        composable(Routes.STOPWATCH) {
            StopwatchScreen()
        }

        composable(Routes.VIDEO_CALL) {
            VideoCallScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}