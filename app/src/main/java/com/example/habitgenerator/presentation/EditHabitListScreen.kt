package com.example.habitgenerator.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditHabitListScreenRoot(
    viewModel: EditHabitListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent
    EditHabitListScreen(state = state, onEvent = onEvent)
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
        floatingActionButton = { MultiFab(Modifier.padding(bottom = 32.dp), onEvent = onEvent) }

    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxWidth()
        ) {
            items(state.habits) {
                EditHabit(habit = it.first, expanded = it.second, onEvent)
            }
        }

    }
}

@Composable
fun MultiFab(
    modifier: Modifier = Modifier,
    onEvent: (EditHabitListEvent) -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val getStringFromClip = { clipboardManager.getText()?.text }
    Column(horizontalAlignment = Alignment.End, modifier = modifier) {
        FloatingActionButton(onClick = {
            onEvent(EditHabitListEvent.ParseFromClipboard(getStringFromClip))
        }
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "")
        }
        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(onClick = {
            onEvent(EditHabitListEvent.ParseHabits { json ->
                clipboardManager.setText(AnnotatedString(json))
            })
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }
        ) {
            Icon(Icons.Filled.Done, contentDescription = "")
        }
        Spacer(modifier = Modifier.height(16.dp))
        ExtendedFloatingActionButton(
            onClick = {
                onEvent(EditHabitListEvent.NewHabit)
            },
            modifier = Modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Add Habit")
                Spacer(modifier.width(4.dp))
                Icon(Icons.Filled.Add, contentDescription = "")
            }
        }
    }

}