package com.example.data

import com.google.gson.internal.LinkedTreeMap

interface IRemoteRepository {

    suspend fun deleteHabit(habit: HabitDataDao)

    suspend fun getHabits(): List<HabitDataDao>

    suspend fun putHabit(habit: HabitDataDao): LinkedTreeMap<String, String>

    suspend fun postHabit(habit: HabitDataDao)
}