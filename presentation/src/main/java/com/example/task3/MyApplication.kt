package com.example.task3

import android.app.Application
import androidx.room.Room
import com.example.task3.DbRoom.HabitDb

class MyApplication: Application() {

    companion object {
        lateinit var db: HabitDb
    }

    override fun onCreate() {
        super.onCreate()



        db = Room.databaseBuilder(
            applicationContext,
            HabitDb::class.java, "database")
            .build()
    }
}