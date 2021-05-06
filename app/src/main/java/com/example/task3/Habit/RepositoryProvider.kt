package com.example.task3.Habit

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call

object RepositoryProvider {
    fun provideRepository(): SearchRepository {
        return SearchRepository(HabitService.create())
    }
}

class SearchRepository(private val apiService: HabitService) {

    companion object{
        const val userToken = "0cfe4cac-170d-4f9f-9b50-ce8e112b57f7"
    }
    fun getHabits(): Call<ArrayList<Habit>> {
        return apiService.listHabits(userToken)
    }

    fun putHabit(habit: Habit):Call<LinkedTreeMap<String, String>> {
        return apiService.putHabit(userToken,habit )
    }

    fun deleteHabit(habit: Habit): Call<Void>{
        return apiService.deleteHabit(userToken, LinkedTreeMap<String,String>().also { it["uid"] = habit.uid })
    }
}