package com.example.data

import com.google.gson.internal.LinkedTreeMap
import javax.inject.Inject


class ApiRepository(private val apiService: HabitService): IRemoteRepository {

    companion object{
        const val userToken = "0cfe4cac-170d-4f9f-9b50-ce8e112b57f7"
    }

    override suspend fun getHabits(): List<HabitDbDao> {
        return apiService.listHabits(userToken)
    }

    override suspend fun putHabit(habit: HabitDbDao): LinkedTreeMap<String, String> {
        return apiService.putHabit(userToken,habit )
    }

    override suspend fun postHabit(habit: HabitDbDao) {
        return apiService.postHabit(userToken, LinkedTreeMap<String,String>().also {
            it["date"] = habit.date.toString()
            it["uid"] = habit.uid
        }/*, LinkedTreeMap<String, Int>().also {it["date"] = habit.date}*/ )
    }

    override suspend fun deleteHabit(habit: HabitDbDao){
        apiService.deleteHabit(userToken, LinkedTreeMap<String,String>().also { it["uid"] = habit.uid })
    }
}