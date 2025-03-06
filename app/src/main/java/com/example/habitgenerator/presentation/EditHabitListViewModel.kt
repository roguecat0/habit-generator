package com.example.habitgenerator.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.habitgenerator.services.Habit
import com.example.habitgenerator.services.HabitService
import com.example.habitgenerator.services.util.splitPairs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

const val TAG = "EditHabitListViewModel"

class EditHabitListViewModel(
    private val habitService: HabitService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(EditHabitListState())
    val state = _state.asStateFlow()

    fun onEvent(event: EditHabitListEvent) {
        Log.d(TAG, "onEvent: event: $event")
        Log.d(TAG, "onEvent: start: ${_state.value}")
        when (event) {
            is EditHabitListEvent.ChangeHabitName -> {
                changeHabitName(event.name, event.id)
            }

            is EditHabitListEvent.NewHabit -> {
                newHabit()
            }

            is EditHabitListEvent.ToggleHabitEnabled -> {
                toggleHabitEnabled(event.id)
            }

            is EditHabitListEvent.ToggleHabitExpanded -> {
                toggleHabitExpand(event.id)
            }

            is EditHabitListEvent.DeleteHabit -> deleteHabit(event.id)
            is EditHabitListEvent.ChangeHabitStartFrom -> {
                changeHabitStartFrom(event.startFrom, event.id)
            }

            is EditHabitListEvent.ChangeHabitStreakName -> {
                changeHabitStreakName(event.name, event.id, event.streakNameIndex)
            }

            is EditHabitListEvent.ChangeHabitStreakValue -> {
                changeHabitStreakValue(event.start, event.id, event.streakNameIndex)
            }

            is EditHabitListEvent.AddHabitStreakName -> {
                addHabitStreakName(event.id)
            }

            is EditHabitListEvent.DeleteHabitStreakName -> {
                deleteHabitStreak(event.id, event.streakNameIndex)
            }
        }
        Log.d(TAG, "onEvent: ${_state.value}")
    }

    private fun deleteHabit(id: Int) {
        _state.value = _state.value.copy(
            habits = _state.value.habits
                .filter { uiHabit -> uiHabit.first.id != id }
        )
    }

    private fun deleteHabitStreak(id: Int, streakIndex: Int) {
        changeAHabitValue(id = id) {
            habitService.deleteHabitStreak(it, streakIndex)
        }

    }

    private fun changeHabitStartFrom(startFrom: String, id: Int) {
        changeAHabitValue(id = id) {
            habitService.changeHabitStartFrom(it, startFrom)
        }
    }

    private fun changeAHabitValue(id: Int, operation: (Habit) -> Habit) {
        val (habits, expandedItems) = _state.value.habits.splitPairs()
        val uiHabits = habitService
            .mapHabitAtId(habits, id, operation)
            .zip(expandedItems)
        _state.value = _state.value.copy(habits = uiHabits)
    }

    private fun addHabitStreakName(id: Int) {
        changeAHabitValue(id = id) { habit ->
            habitService.addStreakName(habit)
        }
    }

    private fun changeHabitName(name: String, id: Int) {
        changeAHabitValue(id = id) { habit ->
            habitService.changeHabitName(habit, name)
        }
    }

    private fun changeHabitStreakName(start: String, id: Int, index: Int) {
        changeAHabitValue(id = id) { habit ->
            habitService.changeHabitStreakName(habit, start, index)
        }
    }

    private fun changeHabitStreakValue(start: String, id: Int, index: Int) {
        changeAHabitValue(id = id) { habit ->
            habitService.changeHabitStreakValue(habit, start, index)
        }
    }

    private fun toggleHabitEnabled(id: Int) {
        changeAHabitValue(id = id) { habit ->
            habitService.toggleHabitEnabled(habit)
        }
    }

    private fun toggleHabitExpand(id: Int) {
        _state.value = _state.value.copy(
            habits = _state.value.habits
                .map { uiHabit ->
                    if (uiHabit.first.id == id) {
                        uiHabit.first to !uiHabit.second
                    } else {
                        uiHabit
                    }
                }
        )
    }

    private fun newHabit() {
        val (habits, _) = _state.value.habits.splitPairs()
        val id = habitService.getNewId(habits)
        val newUiHabit = Habit(id = id) to false
        val inter = _state.value.habits + newUiHabit
        _state.value = _state.value
            .copy(habits = inter)
    }


}