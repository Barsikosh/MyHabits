package com.example.data

import com.google.gson.internal.LinkedTreeMap
import javax.inject.Inject


class ApiRepository(private val apiService: HabitService) : IRemoteRepository {

    override suspend fun getHabits(): List<HabitDbDao> {
        return apiService.listHabits()
    }

    override suspend fun putHabit(habit: HabitDbDao): LinkedTreeMap<String, String> {
        return apiService.putHabit(habit)
    }

    override suspend fun postHabit(habit: HabitDbDao) {
        return apiService.postHabit(LinkedTreeMap<String, Any>().also {
            it["date"] = habit.date
            it["habit_uid"] = habit.uid
        })
    }

    override suspend fun deleteHabit(habit: HabitDbDao) {
        apiService.deleteHabit(LinkedTreeMap<String, String>().also { it["uid"] = habit.uid })
    }
}