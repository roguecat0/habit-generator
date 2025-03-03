package com.example.habitgenerator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.habitgenerator.services.MyService

class MainViewModel(
    private val i: Int = 0,
    val myService: MyService
) : ViewModel() {
    fun doSomething() {
        Log.d("pass", "doSomething: did something")
    }
    fun returnSomething(): String {
        return "Something: " + myService.serviceString()
    }
}