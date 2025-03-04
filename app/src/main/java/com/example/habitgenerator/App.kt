package com.example.habitgenerator

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitgenerator.presentation.EditHabitListScreenRoot
import com.example.habitgenerator.ui.theme.HabitGeneratorTheme

@Composable
fun App() {
    HabitGeneratorTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Screen.EditHabitListScreen) {
            composable<Screen.EditHabitListScreen> { EditHabitListScreenRoot() }
        }
    }

}