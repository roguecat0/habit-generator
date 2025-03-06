package com.example.habitgenerator.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
            when (val habitType = habit.habitType) {
                is HabitType.SingleHabit -> SingleHabitPart(habit, onEvent, habitType)
                else -> {}
            }
        }
    }
}

@Composable
fun SingleHabitPart(
    habit: Habit,
    onEvent: (EditHabitListEvent) -> Unit,
    singlePart: HabitType.SingleHabit
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
        for ((i, pair) in singlePart.streakNames.withIndex()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                OutlinedTextField(
                    value = pair.second,
                    onValueChange = {
                        onEvent(
                            EditHabitListEvent.ChangeHabitStreakName(
                                it, habit.id, i
                            )
                        )
                    },
                    label = { Text("name") },
                    modifier = Modifier.fillMaxWidth(.4f)
                )
                OutlinedTextField(
                    value = if (pair.first == 0) {
                        ""
                    } else {
                        pair.first.toString()
                    },
                    onValueChange = {
                        onEvent(EditHabitListEvent.ChangeHabitStreakValue(it, habit.id, i))
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("from") },
                    modifier = Modifier.fillMaxWidth(.4f)
                )
                IconButton(onClick = {
                    onEvent(
                        EditHabitListEvent.DeleteHabitStreakName(
                            habit.id,
                            i
                        )
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = ""
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            IconButton(
                onClick = { onEvent(EditHabitListEvent.AddHabitStreakName(habit.id)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }

        }
    }
}

@Preview
@Composable
private fun EditHabitPreview() {
    EditHabit()
}