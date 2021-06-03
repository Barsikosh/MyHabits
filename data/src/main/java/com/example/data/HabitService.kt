package com.example.data


import com.google.gson.internal.LinkedTreeMap
import retrofit2.http.*

interface HabitService {

    @GET("api/habit")
    suspend fun listHabits(): List<HabitDataDao>

    @PUT("api/habit")
    suspend fun putHabit(
        @Body habit: HabitDataDao
    ):  LinkedTreeMap<String, String>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(
        @Body uid: LinkedTreeMap<String, String>
    )

    @POST("api/habit_done")
    suspend fun postHabit(
        @Body uidAndDate: LinkedTreeMap<String, Any>
    )
}