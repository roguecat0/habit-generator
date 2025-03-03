package com.example.habitgenerator

import android.app.Application
import android.util.Log
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            Log.d("works", "onCreate: starting koin")
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }

}