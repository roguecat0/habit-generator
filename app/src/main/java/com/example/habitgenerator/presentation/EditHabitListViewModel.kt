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

            is EditHabitListEvent.ParseHabits -> {
                parseHabitsToClipboard(event.clipboardCopy)
            }

            is EditHabitListEvent.ParseFromClipboard -> {
                parseFromHabitsFromClipboard(event.getStringFromClip)
            }

            is EditHabitListEvent.RotateHabitType -> {
                rotateHabitType(event.id)
            }

            is EditHabitListEvent.AddWeekScheduledHabit -> {
                addWeekHabit(event.id)
            }

            is EditHabitListEvent.AddIntervalScheduledHabit -> {
                addIntervalHabit(event.id)
            }

            is EditHabitListEvent.ToggleScheduledHabitEnabled -> {
                toggleScheduledHabitEnabled(event.id, event.index)
            }

            is EditHabitListEvent.DeleteScheduledHabit -> {
                deleteScheduledHabit(event.id, event.index)
            }

            is EditHabitListEvent.ChangeScheduledHabitName -> {
                changeScheduledHabitName(event.id, event.index, event.name)
            }

            is EditHabitListEvent.ToggleWeekdayEnabled -> {
                toggleWeekdayEnabled(event.id, event.scheduledIndex, event.weekdayIndex)
            }

            is EditHabitListEvent.ChangeIntervalAmount -> {
                changeIntervalAmount(event.id, event.index, event.interval)

            }
        }
        Log.d(TAG, "onEvent: ${_state.value}")
    }

    private fun changeIntervalAmount(id: Int, index: Int, interval: String) {
        changeAHabitValue(id = id) {
            habitService.changeIntervalAmount(it, index, interval)
        }
    }

    private fun toggleWeekdayEnabled(id: Int, scheduledIndex: Int, weekdayIndex: Int) {
        changeAHabitValue(id = id) {
            habitService.toggleWeekdayEnabled(it, scheduledIndex, weekdayIndex)
        }
    }

    private fun changeScheduledHabitName(id: Int, index: Int, name: String) {
        changeAHabitValue(id = id) {
            habitService.changeScheduledHabitName(it, index, name)
        }
    }

    private fun toggleScheduledHabitEnabled(id: Int, index: Int) {
        changeAHabitValue(id = id) {
            habitService.toggleScheduledHabitEnabled(it, index)
        }
    }

    private fun deleteScheduledHabit(id: Int, index: Int) {
        changeAHabitValue(id = id) {
            habitService.deleteScheduledHabit(it, index)
        }
    }

    private fun addWeekHabit(id: Int) {
        changeAHabitValue(id = id) {
            habitService.addScheduledWeek(it)
        }
    }

    private fun addIntervalHabit(id: Int) {
        changeAHabitValue(id) {
            habitService.addScheduledInterval(it)
        }
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

    private fun rotateHabitType(id: Int) {
        changeAHabitValue(id = id) {
            habitService.rotateType(it)
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

    private fun parseHabitsToClipboard(clipboardCopy: (String) -> Unit) {
        val (habits, _) = _state.value.habits.splitPairs()
        val json = habitService.parseHabitsToJsonWithSpecials(
            habits,
            _state.value.specials
        )
        Log.d(TAG, "parseHabitsToClipboard: $json")
        clipboardCopy(json)
    }

    private fun parseFromHabitsFromClipboard(getStringFromClip: () -> String?) {
        getStringFromClip()?.let { json ->
            val (habits, specials) = habitService.parseHabitsFromJsonWithJsonAddition(json)
            _state.value = EditHabitListState(
                habits = habits.zip(habits.map { false }),
                specials = specials,
            )
        }
    }

}