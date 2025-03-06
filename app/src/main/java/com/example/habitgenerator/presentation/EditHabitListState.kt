package com.example.habitgenerator.presentation

import com.example.habitgenerator.services.Habit

data class EditHabitListState(
    val habits: List<Pair<Habit, Boolean>> = emptyList()
)

sealed interface EditHabitListEvent {
    data class ChangeHabitName(val name: String, val id: Int) : EditHabitListEvent
    data class ChangeHabitStreakName(val name: String, val id: Int, val streakNameIndex: Int) :
        EditHabitListEvent

    data class ChangeHabitStreakValue(val start: String, val id: Int, val streakNameIndex: Int) :
        EditHabitListEvent

    data class AddHabitStreakName(val id: Int) : EditHabitListEvent
    data class ToggleHabitEnabled(val id: Int) : EditHabitListEvent
    data class ToggleHabitExpanded(val id: Int) : EditHabitListEvent
    data class DeleteHabit(val id: Int) : EditHabitListEvent
    data class ChangeHabitStartFrom(val startFrom: String, val id: Int) : EditHabitListEvent

    data object NewHabit : EditHabitListEvent
}