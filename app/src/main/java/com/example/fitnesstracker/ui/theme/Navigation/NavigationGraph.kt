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
import com.example.fitnesstracker.viewmodel.StopwatchViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fitnesstracker.viewmodel.StopwatchViewModel
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
            val isDarkTheme by stepViewModel.isDarkTheme.collectAsState()
            val profileImageUri by stepViewModel.profileImageUri.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            // Pass the state and event handlers to the screen
            ProfileScreen(
                userProfile = userProfile,
                isDarkTheme = isDarkTheme,
                profileImageUri = profileImageUri,
                onSaveProfile = { updatedProfile ->
                    coroutineScope.launch {
                        stepViewModel.saveUserProfile(updatedProfile)
                    }
                },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                onThemeToggle = { stepViewModel.toggleTheme() },
                onSaveImage = { uri -> stepViewModel.saveProfileImage(uri) }
            )
        }

        composable(Routes.STOPWATCH) {
            val stopwatchViewModel: StopwatchViewModel = viewModel(factory = StopwatchViewModelFactory())
            StopwatchScreen(viewModel = stopwatchViewModel)
        }

        composable(Routes.VIDEO_CALL) {
            VideoCallScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}