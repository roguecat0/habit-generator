package com.example.habitgenerator

import com.example.habitgenerator.services.MyService
import com.example.habitgenerator.ui.viewmodels.MainViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<MyService> { MyService() }
    viewModel {MainViewModel(myService = get())}
}