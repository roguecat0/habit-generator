package com.example.habitgenerator.presentation

import com.example.habitgenerator.services.Habit

data class EditHabitListState(
    val habits: List<Pair<Habit, Boolean>> = emptyList()
)

sealed interface EditHabitListEvent {
    data class ChangeHabitName(val name: String, val id: Int) : EditHabitListEvent
    data class ToggleHabitEnabled(val id: Int) : EditHabitListEvent
    data class ToggleHabitExpanded(val id: Int) : EditHabitListEvent
    data object NewHabit : EditHabitListEvent
}