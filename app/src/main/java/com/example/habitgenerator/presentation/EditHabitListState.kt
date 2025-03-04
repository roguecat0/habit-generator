package com.example.habitgenerator.presentation

data class EditHabitListState(val tmp: Int = 0)

sealed interface EditHabitListEvent {
    data object Nothing: EditHabitListEvent
}