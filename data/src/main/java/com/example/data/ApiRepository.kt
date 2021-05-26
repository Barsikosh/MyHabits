package com.example.data

import com.google.gson.internal.LinkedTreeMap


class ApiRepository(private val apiService: HabitService) : IRemoteRepository {

    override suspend fun getHabits(): List<HabitDataDao> {
        return apiService.listHabits()
    }

    override suspend fun putHabit(habit: HabitDataDao): LinkedTreeMap<String, String> {
        return apiService.putHabit(habit)
    }

    override suspend fun postHabit(habit: HabitDataDao) {
        return apiService.postHabit(LinkedTreeMap<String, Any>().also {
            it["date"] = habit.date
            it["habit_uid"] = habit.uid
        })
    }

    override suspend fun deleteHabit(habit: HabitDataDao) {
        apiService.deleteHabit(LinkedTreeMap<String, String>().also { it["uid"] = habit.uid })
    }
}