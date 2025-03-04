package com.example.habitgenerator.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.habitgenerator.Screen
import com.example.habitgenerator.services.HabitService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditHabitListViewModel(
    private val habitService: HabitService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val _state = MutableStateFlow(EditHabitListState())
    val state = _state.asStateFlow()
    fun returnSomething(): String {
        return "\n\n\nSomething: " +
                "\n"+savedStateHandle.keys() +
                "\n" + savedStateHandle.toRoute<Screen.EditHabitListScreen>()
    }
    fun onEvent(onEvent: EditHabitListEvent) { }
}