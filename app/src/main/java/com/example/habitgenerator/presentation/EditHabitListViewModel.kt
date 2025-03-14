package com.example.habitgenerator.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.habitgenerator.Screen
import com.example.habitgenerator.data_layer.Habit
import com.example.habitgenerator.data_layer.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


const val TAG = "EditHabitListViewModel"

class EditHabitListViewModel(
    private val habitRepository: HabitRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(EditHabitListState())
    private val _habits = habitRepository.getHabits2()
    val state: StateFlow<EditHabitListState> = combine(_state, _habits) { state, habits ->
        state.copy(
            habits = habits.map { habit ->
                habit to state.indexExpandedHabits.contains(habit.id)
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), EditHabitListState())

    init {
        val id = savedStateHandle.toRoute<Screen.EditHabitListScreen>()
        Log.d("tag", "id: $id")
    }

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
            habitRepository.changeIntervalAmount(it, index, interval)
        }
    }

    private fun toggleWeekdayEnabled(id: Int, scheduledIndex: Int, weekdayIndex: Int) {
        changeAHabitValue(id = id) {
            habitRepository.toggleWeekdayEnabled(it, scheduledIndex, weekdayIndex)
        }
    }

    private fun changeScheduledHabitName(id: Int, index: Int, name: String) {
        changeAHabitValue(id = id) {
            habitRepository.changeScheduledHabitName(it, index, name)
        }
    }

    private fun toggleScheduledHabitEnabled(id: Int, index: Int) {
        changeAHabitValue(id = id) {
            habitRepository.toggleScheduledHabitEnabled(it, index)
        }
    }

    private fun deleteScheduledHabit(id: Int, index: Int) {
        changeAHabitValue(id = id) {
            habitRepository.deleteScheduledHabit(it, index)
        }
    }

    private fun addWeekHabit(id: Int) {
        changeAHabitValue(id = id) {
            habitRepository.addScheduledWeek(it)
        }
    }

    private fun addIntervalHabit(id: Int) {
        changeAHabitValue(id) {
            habitRepository.addScheduledInterval(it)
        }
    }

    private fun deleteHabit(id: Int) {
        habitRepository.deleteHabit(id)
        _state.value = _state.value.copy(
            indexExpandedHabits = _state.value.indexExpandedHabits.filter { it != id }
        )
    }

    private fun deleteHabitStreak(id: Int, streakIndex: Int) {
        changeAHabitValue(id = id) {
            habitRepository.deleteHabitStreak(it, streakIndex)
        }

    }

    private fun changeHabitStartFrom(startFrom: String, id: Int) {
        changeAHabitValue(id = id) {
            habitRepository.changeHabitStartFrom(it, startFrom)
        }
    }

    private fun rotateHabitType(id: Int) {
        changeAHabitValue(id = id) {
            habitRepository.rotateType(it)
        }
    }

    private fun changeAHabitValue(id: Int, operation: (Habit) -> Habit) {
        habitRepository.changeAHabitValue(id, operation)
    }

    private fun addHabitStreakName(id: Int) {
        changeAHabitValue(id = id) { habit ->
            habitRepository.addStreakName(habit)
        }
    }

    private fun changeHabitName(name: String, id: Int) {
        changeAHabitValue(id = id) { habit ->
            habitRepository.changeHabitName(habit, name)
        }
    }

    private fun changeHabitStreakName(start: String, id: Int, index: Int) {
        changeAHabitValue(id = id) { habit ->
            habitRepository.changeHabitStreakName(habit, start, index)
        }
    }

    private fun changeHabitStreakValue(start: String, id: Int, index: Int) {
        changeAHabitValue(id = id) { habit ->
            habitRepository.changeHabitStreakValue(habit, start, index)
        }
    }

    private fun toggleHabitEnabled(id: Int) {
        changeAHabitValue(id = id) { habit ->
            habitRepository.toggleHabitEnabled(habit)
        }
    }

    private fun toggleHabitExpand(id: Int) {
        _state.value = _state.value.copy(
            indexExpandedHabits = if (_state.value.indexExpandedHabits.contains(id)) {
                _state.value.indexExpandedHabits.filter { id != it }
            } else {
                _state.value.indexExpandedHabits + id
            }
        )
        Log.d(TAG, "toggleHabitExpand: ${_state.value.indexExpandedHabits}")
    }

    private fun newHabit() {
        habitRepository.addNewHabit()
    }

    private fun parseHabitsToClipboard(clipboardCopy: (String) -> Unit) {
        val json = habitRepository.parseToJson()
        Log.d(TAG, "parseHabitsToClipboard: $json")
        clipboardCopy(json)
    }

    private fun parseFromHabitsFromClipboard(getStringFromClip: () -> String?) {
        getStringFromClip()?.let { json ->
            habitRepository.parseFromJson(json)
        }
    }
}