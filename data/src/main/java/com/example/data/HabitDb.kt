package com.example.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitDataDao::class], version = 2,  exportSchema = false)
abstract class HabitDb: RoomDatabase() {
  abstract fun HabitDao(): HabitDao
}