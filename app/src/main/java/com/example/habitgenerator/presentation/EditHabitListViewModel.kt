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
        Log.d(TAG, "onEvent: $event")
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
        }
    }

    private fun changeAHabitValue(id: Int, operation: (Habit) -> Habit) {
        val (habits, expandedItems) = _state.value.habits.splitPairs()
        val uiHabits = habitService
            .mapHabitAtId(habits, id, operation)
            .zip(expandedItems)
        _state.value = _state.value.copy(habits = uiHabits)
    }

    private fun changeHabitName(name: String, id: Int) {
        changeAHabitValue(id = id) {
            habitService.changeHabitName(it, name)
        }
    }

    private fun toggleHabitEnabled(id: Int) {
        changeAHabitValue(id = id) {
            habitService.toggleHabitEnabled(it)
        }
    }

    private fun toggleHabitExpand(id: Int) {
        _state.value = _state.value.copy(
            habits = _state.value.habits
                .map {
                    if (it.first.id == id) {
                        it.first to !it.second
                    } else {
                        it
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