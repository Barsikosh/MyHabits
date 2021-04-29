package com.example.task3.DbRoom

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.task3.Habit


@Dao
@TypeConverters(Habit.TypeConverter::class, Habit.PriorityConverter::class)
@Suppress("AndroidUnresolvedRoomSqlReference")

interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT * FROM Habit WHERE id = :id")
    fun getById(id: Long): LiveData<Habit?>

    @Query("SELECT * FROM Habit WHERE type = :habitType")
    fun getByType(habitType: Habit.HabitType): LiveData<List<Habit>>

    @Insert
    fun insert(habit: Habit?)

    @Update
    fun update(habit: Habit?)

    @Delete
    fun delete(habit: Habit?)
}