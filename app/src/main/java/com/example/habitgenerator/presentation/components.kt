package com.example.habitgenerator.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitgenerator.services.Habit
import com.example.habitgenerator.services.HabitType
import kotlin.math.exp

@Composable
fun EditHabit(
    habit: Habit = Habit(),
    expanded: Boolean = true,
    onEvent: (EditHabitListEvent) -> Unit = {},
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val (symbol, label) = when (habit.habitType) {
                is HabitType.SingleHabit -> {
                    listOf("S", "Single Habit")
                }

                else -> {
                    listOf("", "")
                }
            }
            CircleShape(symbol)
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = habit.name,
                onValueChange = {
                    onEvent(EditHabitListEvent.ChangeHabitName(it, habit.id))
                },
                label = { Text(label) },
                modifier = Modifier.fillMaxWidth(fraction = 0.6f)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = habit.enabled,
                    onCheckedChange = {
                        onEvent(EditHabitListEvent.ToggleHabitEnabled(habit.id))
                    }
                )
                IconButton(onClick = {
                    onEvent(EditHabitListEvent.ToggleHabitExpanded(habit.id))
                }) {
                    Icon(
                        imageVector = if (expanded) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = ""
                    )
                }
            }
        }
        if (expanded) {
            SingleHabitPart(habit, onEvent)
        }
    }
}

@Composable
fun SingleHabitPart(
    habit: Habit,
    onEvent: (EditHabitListEvent) -> Unit,
    singlePart: HabitType.SingleHabit = HabitType.SingleHabit(
        hashMapOf(Pair(3, "first"))
    )
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        ) {

            OutlinedTextField(
                value = if (habit.startFrom == 0) {
                    ""
                } else {
                    habit.startFrom.toString()
                },
                onValueChange = { onEvent(EditHabitListEvent.ChangeHabitStartFrom(it, habit.id)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text("start from") }
            )
            IconButton(onClick = { onEvent(EditHabitListEvent.DeleteHabit(habit.id)) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = ""
                )
            }
        }
        singlePart.streakName?.let { streakNames ->
            for (pair in streakNames.toList()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    OutlinedTextField(
                        value = pair.second,
                        onValueChange = {},
                        label = { Text("name") }
                    )
                    OutlinedTextField(
                        value = pair.first.toString(),
                        onValueChange = {},
                        label = { Text("start from") }
                    )
                }

            }
        }
    }
}

@Preview
@Composable
private fun EditHabitPreview() {
    EditHabit()
}