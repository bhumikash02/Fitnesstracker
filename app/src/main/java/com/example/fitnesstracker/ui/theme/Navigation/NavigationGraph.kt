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

        composable(Routes.HOME) {
            HomeScreen(
                onWorkoutClick = { navController.navigate(Routes.WORKOUT) },
                onBmiClick = { navController.navigate(Routes.BMI) },
                onStepsClick = { navController.navigate(Routes.STEP_COUNTER) }
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

        // THIS IS THE NEW BLOCK YOU ARE ADDING
        composable(Routes.PROFILE) {
            // Get the user profile state from the ViewModel
            val userProfile by stepViewModel.userProfile.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            ProfileScreen(
                userProfile = userProfile,
                onSaveProfile = { updatedProfile ->
                    // When the save button is clicked, tell the ViewModel to save the data
                    coroutineScope.launch {
                        stepViewModel.saveUserProfile(updatedProfile)
                    }
                },
                onLogout = {
                    // When logout is clicked, navigate to the login screen and clear all previous screens
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                },
                // Placeholders for dark theme switch. You can implement these later.
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