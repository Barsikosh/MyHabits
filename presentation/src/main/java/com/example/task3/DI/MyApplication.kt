package com.example.task3.DI

import android.app.Application
import com.example.data.HabitDb

class MyApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent
    private set


    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }
}