package com.example.task3.DbRoom

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.task3.Habit.Habit

@Dao
@TypeConverters(Habit.TypeConverter::class, Habit.PriorityConverter::class)
@Suppress("AndroidUnresolvedRoomSqlReference")

interface HabitDao {
    @Query("SELECT * FROM habit")
    fun getAll(): LiveData<List<Habit>>

    @Query("SELECT  * FROM Habit WHERE uid LIKE :uid")
    fun getById(uid: String?): Habit?

    @Query("SELECT  * FROM Habit WHERE name LIKE :name")
    fun getByName(name: String): Habit?

    @Query("SELECT * FROM Habit WHERE type = :habitType")
    fun getByType(habitType: Habit.HabitType): LiveData<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: Habit?): Long

    @Update
    fun update(habit: Habit?)

    @Delete
    fun delete(habit: Habit?)
}