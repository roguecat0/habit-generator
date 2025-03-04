package com.example.habitgenerator

import com.example.habitgenerator.services.HabitService
import com.example.habitgenerator.services.MyService
import com.example.habitgenerator.presentation.EditHabitListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<MyService> { MyService() }
    single {HabitService()}
    viewModel { EditHabitListViewModel(get(), get()) }
}