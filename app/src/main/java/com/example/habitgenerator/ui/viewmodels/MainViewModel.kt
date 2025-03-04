package com.example.habitgenerator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.habitgenerator.Screen
import com.example.habitgenerator.services.MyService

class MainViewModel(
    private val i: Int = 0,
    private val myService: MyService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    fun doSomething() {
        Log.d("pass", "doSomething: did something")
    }
    fun returnSomething(): String {
        return "\n\n\nSomething: " + myService.serviceString() +
                "\n"+savedStateHandle.keys() +
                "\n" + savedStateHandle.toRoute<Screen.MainScreen>()
    }
}