package com.example.habitgenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habitgenerator.ui.theme.HabitGeneratorTheme
import com.example.habitgenerator.ui.viewmodels.MainViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitGeneratorTheme {
                val navController = rememberNavController()
                MyNavHost(navController)
            }
        }
    }
}
sealed interface Screen {
    // needs to be a lib also needs to be a plugin...
    @Serializable
    data class MainScreen(val hello: String): Screen
}

@Composable
fun MyNavHost(
    navController: NavHostController,
) {
    NavHost(navController, startDestination = Screen.MainScreen("cool")) {
        composable<Screen.MainScreen> { Greeting(name = "lol") }
    }
}

@Composable
fun Greeting(
    name: String, modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
 ) {
    Text(
        text = "Hello $name! ${viewModel.returnSomething()}",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabitGeneratorTheme {
        Greeting("Android")
    }
}