package com.example.domain.repository

import com.example.domain.entities.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {

    suspend fun addItem(habit: Habit)

    suspend fun updateItem(newHabit: Habit)

    suspend fun removeItem(habit: Habit)

    suspend fun removeFromDb(habit: Habit)

    suspend fun postItem(habit: Habit)

    fun getLocalData() : Flow<List<Habit>>

/*
    fun getRemoteData() : List<Habit>?
*/

}