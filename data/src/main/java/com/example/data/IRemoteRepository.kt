package com.example.data

import com.google.gson.internal.LinkedTreeMap

interface IRemoteRepository {

    suspend fun deleteHabit(habit: HabitDbDao)

    suspend fun getHabits(): List<HabitDbDao>

    suspend fun putHabit(habit: HabitDbDao): LinkedTreeMap<String, String>
}