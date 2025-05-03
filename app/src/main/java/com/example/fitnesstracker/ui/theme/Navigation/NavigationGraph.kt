package com.example.fitnesstracker.ui.theme.Navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.fitnesstracker.ui.theme.screen.*
import com.example.fitnesstracker.viewmodel.StepViewModel
import com.example.fitnesstracker.viewmodel.WorkoutViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    stepViewModel: StepViewModel // ✅ Fix: add this!
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
            StepCounterScreen(viewModel = stepViewModel) // StepViewModel is internal to this
        }

        composable(Routes.PROFILE) {
            ProfileScreen(viewModel = stepViewModel) // ✅ Pass here
        }

        composable(Routes.VIDEO_CALL) {
            VideoCallScreen()
        }

        composable(Routes.STOPWATCH) {
            StopwatchScreen()
        }
    }
}
