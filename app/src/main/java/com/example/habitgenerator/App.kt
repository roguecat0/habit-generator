package com.example.habitgenerator

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.example.habitgenerator.presentation.EditHabitListScreenRoot
import com.example.habitgenerator.ui.theme.HabitGeneratorTheme
import kotlinx.serialization.Serializable

const val DEEP_LINK_DOMAIN = "habit-gen.com"

sealed interface Screen {
    // needs to be a lib also needs to be a plugin...
    @Serializable
    data class EditHabitListScreen(val tag: Int?) : Screen
}


@Composable
fun App() {
    HabitGeneratorTheme {
        val navController = rememberNavController()

        NavHost(
            navController,
            startDestination = Screen.EditHabitListScreen(null)
        ) {
            composable<Screen.EditHabitListScreen>(
                deepLinks = listOf(
                    navDeepLink<Screen.EditHabitListScreen>(
                        basePath = "api://$DEEP_LINK_DOMAIN"
                    )
                )
            ) {
                EditHabitListScreenRoot()
            }
        }
    }

}