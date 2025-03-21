package com.example.habitgenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.habitgenerator.ui.theme.HabitGeneratorTheme
import com.example.habitgenerator.presentation.EditHabitListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.data?.let { uri ->
            Log.d("DeepLink", "Received deep link: $uri")
        }
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}


@Composable
fun Greeting(
    name: String, modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
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