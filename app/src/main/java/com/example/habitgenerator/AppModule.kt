package com.example.habitgenerator

import com.example.habitgenerator.data_layer.HabitRepository
import com.example.habitgenerator.data_layer.MyService
import com.example.habitgenerator.presentation.EditHabitListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<MyService> { MyService() }
    single { HabitRepository() }
    viewModel { EditHabitListViewModel(get(), get()) }
}