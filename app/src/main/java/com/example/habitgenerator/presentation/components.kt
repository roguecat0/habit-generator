package com.example.habitgenerator.presentation

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.FilterChip
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
import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitType
import com.example.habitgenerator.data_layer.ScheduledHabit
import com.example.habitgenerator.data_layer.ScheduledType

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
                    listOf("X", "Scheduled Habit")
                }
            }
            CircleShape(
                symbol,
                modifier = Modifier.clickable {
                    onEvent(EditHabitListEvent.RotateHabitType(habit.id))
                }
            )
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
            ExpandedPart(habit, onEvent)
        }
    }
}

@Composable
fun ExpandedPart(
    habit: Habit,
    onEvent: (EditHabitListEvent) -> Unit,
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
        when (val habitType = habit.habitType) {
            is HabitType.SingleHabit -> SingleHabitPart(habit, onEvent, habitType)
            is HabitType.Scheduled -> ScheduledHabitPart(habit, onEvent, habitType)
            else -> {}
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
        for ((i, pair) in singlePart.streakNames.withIndex()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(start = 16.dp)
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

@Composable
fun ScheduledHabitPart(
    habit: Habit,
    onEvent: (EditHabitListEvent) -> Unit,
    scheduledPart: HabitType.Scheduled
) {
    Column {
        for ((i, scheduled) in scheduledPart.scheduledHabits.withIndex()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(start = 16.dp)

            ) {
                OutlinedTextField(
                    value = scheduled.name,
                    onValueChange = {
                        onEvent(
                            EditHabitListEvent
                                .ChangeScheduledHabitName(habit.id, i, it)
                        )
                    },
                    label = { Text("scheduled name") },
                    modifier = Modifier.fillMaxWidth(fraction = 0.6f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = scheduled.enabled,
                        onCheckedChange = {
                            onEvent(
                                EditHabitListEvent.ToggleScheduledHabitEnabled(
                                    id = habit.id,
                                    index = i,
                                )
                            )
                        }
                    )
                    IconButton(onClick = {
                        onEvent(
                            EditHabitListEvent.DeleteScheduledHabit(
                                id = habit.id,
                                index = i,
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
            when (val scheduledType = scheduled.scheduledType) {
                is ScheduledType.Weekdays -> {
                    WeekdaysRow(habit, scheduledType, i, onEvent)
                }

                is ScheduledType.Interval -> {
                    IntervalRow(habit, scheduledType, i, onEvent)
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onEvent(EditHabitListEvent.AddWeekScheduledHabit(habit.id)) },
                modifier = Modifier.fillMaxWidth(.3f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Weekdays")
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
            IconButton(
                onClick = {
                    onEvent(EditHabitListEvent.AddIntervalScheduledHabit(habit.id))
                },
                modifier = Modifier.fillMaxWidth(.3f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Interval")
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun IntervalRow(
    habit: Habit,
    interval: ScheduledType.Interval,
    scheduledIndex: Int,
    onEvent: (EditHabitListEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp)
    ) {
        OutlinedTextField(
            value = if (interval.intervalDays == 0) {
                ""
            } else {
                interval.intervalDays.toString()
            },
            onValueChange = {
                onEvent(
                    EditHabitListEvent.ChangeIntervalAmount(
                        habit.id, scheduledIndex, it
                    )
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("interval") },
            modifier = Modifier.fillMaxWidth(.3f)
        )
    }

}

@Composable
fun WeekdaysRow(
    habit: Habit,
    weekdays: ScheduledType.Weekdays,
    scheduledIndex: Int,
    onEvent: (EditHabitListEvent) -> Unit,
) {
    val labelWeekdays = listOf("mo", "tu", "we", "th", "fr", "sa", "su")
    Row(
        horizontalArrangement = Arrangement.spacedBy(
            4.dp, alignment = Alignment.CenterHorizontally
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        for ((i, weekday) in weekdays.activeDays.withIndex()) {
            FilterChip(
                selected = weekday,
                onClick = {
                    onEvent(
                        EditHabitListEvent.ToggleWeekdayEnabled(
                            habit.id, scheduledIndex, i
                        )
                    )
                },
                label = { Text(labelWeekdays[i]) },
                leadingIcon = {}
            )
        }
    }
}

@Preview
@Composable
private fun EditHabitPreview() {
    val single = Habit(
        habitType = HabitType.SingleHabit(
            listOf(
                3 to "hello"
            )
        )
    )
    EditHabit(single)
}

@Preview
@Composable
private fun ScheduledHabitPreview() {
    val scheduled = Habit(
        habitType = HabitType.Scheduled(
            listOf(
                ScheduledHabit(
                    scheduledType = ScheduledType.Weekdays()
                ),
                ScheduledHabit(
                    scheduledType = ScheduledType.Interval()
                )
            )
        )
    )
    EditHabit(scheduled)
}
