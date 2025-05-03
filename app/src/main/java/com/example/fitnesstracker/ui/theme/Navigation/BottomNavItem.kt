package com.example.fitnesstracker.ui.theme.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {

    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)

    object Stopwatch : BottomNavItem("stopwatch", "Stopwatch", Icons.Filled.AccessTime)

    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)

    object VideoCall : BottomNavItem("video_call", "Video Call", Icons.Default.VideoCall)

}
