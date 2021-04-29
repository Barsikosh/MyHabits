package com.example.task3.DbRoom

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.example.task3.Habit

@Database(entities = [Habit::class], version = 2)
abstract class HabitDb: RoomDatabase() {
  abstract fun HabitDao(): HabitDao
}