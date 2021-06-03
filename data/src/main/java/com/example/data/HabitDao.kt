package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow


@Dao
@TypeConverters(HabitDataDao.TypeConverter::class, HabitDataDao.PriorityConverter::class)
@Suppress("AndroidUnresolvedRoomSqlReference")
interface HabitDao {


    @Query("SELECT * FROM HabitDataDao")
    fun getAll(): Flow<List<HabitDataDao>>

    @Query("SELECT  * FROM HabitDataDao WHERE uid LIKE :uid")
    suspend fun getById(uid: String?): HabitDataDao?

    @Query("SELECT  * FROM HabitDataDao WHERE name LIKE :name")
    suspend fun getByName(name: String): HabitDataDao?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: HabitDataDao?)

    @Update
    suspend fun update(habit: HabitDataDao?)

    @Delete
    suspend fun delete(habit: HabitDataDao?)
}