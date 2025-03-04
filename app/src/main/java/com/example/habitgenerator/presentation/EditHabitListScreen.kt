package com.example.habitgenerator.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.habitgenerator.Greeting
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditHabitListScreenRoot(
    viewModel: EditHabitListViewModel = koinViewModel() ) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent
    EditHabitListScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitListScreen(
    state: EditHabitListState = EditHabitListState(),
    onEvent: (EditHabitListEvent) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Edit Habits") }) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {  Greeting(state.toString()) }
    }
}