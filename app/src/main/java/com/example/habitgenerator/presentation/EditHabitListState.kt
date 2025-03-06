package com.example.habitgenerator.presentation

import com.example.habitgenerator.services.Habit

data class EditHabitListState(
    val habits: List<Pair<Habit,Boolean>> = emptyList()
)
sealed interface EditHabitListEvent {
    data object Nothing: EditHabitListEvent
}