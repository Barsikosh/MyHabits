package com.example.task3.Habit

import com.google.gson.internal.LinkedTreeMap
import retrofit2.Call

object RepositoryProvider {

    var searchRepository: SearchRepository? = null

    fun provideRepository(): SearchRepository {
        return if (searchRepository == null){
            searchRepository = SearchRepository(HabitService.create())
            searchRepository!!
        } else searchRepository!!
    }
}

class SearchRepository(private val apiService: HabitService) {

    companion object{
        const val userToken = "0cfe4cac-170d-4f9f-9b50-ce8e112b57f7"
    }
    suspend fun getHabits(): ArrayList<Habit> {
        return apiService.listHabits(userToken)
    }

    suspend fun putHabit(habit: Habit): LinkedTreeMap<String, String> {
        return apiService.putHabit(userToken,habit )
    }

    suspend fun deleteHabit(habit: Habit){
        apiService.deleteHabit(userToken, LinkedTreeMap<String,String>().also { it["uid"] = habit.uid })
    }
}