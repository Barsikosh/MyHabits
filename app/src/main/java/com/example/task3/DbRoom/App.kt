package com.example.task3.DbRoom

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class App: Application() {

    companion object {
        lateinit var instance: App
        lateinit var db: HabitDb
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        db = Room.databaseBuilder(
            applicationContext,
            HabitDb::class.java, "database")
            .allowMainThreadQueries()
            .build()
    }
}