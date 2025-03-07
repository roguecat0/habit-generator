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

    data class DeleteHabitStreakName(val id: Int, val streakNameIndex: Int) :
        EditHabitListEvent

    data class AddHabitStreakName(val id: Int) : EditHabitListEvent
    data class AddWeekScheduledHabit(val id: Int) : EditHabitListEvent
    data class DeleteScheduledHabit(val id: Int, val index: Int) : EditHabitListEvent
    data class ToggleScheduledHabitEnabled(val id: Int, val index: Int) : EditHabitListEvent
    data class ToggleWeekdayEnabled(val id: Int, val scheduledIndex: Int, val weekdayIndex: Int) :
        EditHabitListEvent

    data class ChangeScheduledHabitName(val id: Int, val index: Int, val name: String) :
        EditHabitListEvent

    data class ChangeIntervalAmount(val id: Int, val index: Int, val interval: String) :
        EditHabitListEvent

    data class AddIntervalScheduledHabit(val id: Int) : EditHabitListEvent
    data class RotateHabitType(val id: Int) : EditHabitListEvent
    data class ToggleHabitEnabled(val id: Int) : EditHabitListEvent
    data class ToggleHabitExpanded(val id: Int) : EditHabitListEvent
    data class DeleteHabit(val id: Int) : EditHabitListEvent
    data class ChangeHabitStartFrom(val startFrom: String, val id: Int) : EditHabitListEvent

    data object NewHabit : EditHabitListEvent
    data class ParseHabits(val clipboardCopy: (String) -> Unit) : EditHabitListEvent
    data class ParseFromClipboard(val getStringFromClip: () -> String?) : EditHabitListEvent
}