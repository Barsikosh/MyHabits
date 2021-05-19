package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
@TypeConverters(HabitDbDao.TypeConverter::class, HabitDbDao.PriorityConverter::class)
@Suppress("AndroidUnresolvedRoomSqlReference")
interface HabitDao {


    @Query("SELECT * FROM HabitDbDao")
    fun getAll(): Flow<List<HabitDbDao>>

    @Query("SELECT  * FROM HabitDbDao WHERE uid LIKE :uid")
    fun getById(uid: String?): HabitDbDao?

    @Query("SELECT  * FROM HabitDbDao WHERE name LIKE :name")
    fun getByName(name: String): HabitDbDao?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(habit: HabitDbDao?)

    @Update
    fun update(habit: HabitDbDao?)

    @Delete
    fun delete(habit: HabitDbDao?)
}