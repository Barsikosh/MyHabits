package com.example.task3.DbRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.task3.Habit.Habit

@Database(entities = [Habit::class], version = 2)
abstract class HabitDb: RoomDatabase() {
  abstract fun HabitDao(): HabitDao
}