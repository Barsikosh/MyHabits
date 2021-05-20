package com.example.data


import com.google.gson.internal.LinkedTreeMap
import retrofit2.http.*

interface HabitService {

    @GET("api/habit")
    suspend fun listHabits(@Header("Authorization") token: String): List<HabitDbDao>

    @PUT("api/habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body habit: HabitDbDao
    ): LinkedTreeMap<String, String>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Body uid: LinkedTreeMap<String, String>
    )

    @POST("api/habit_done")
    suspend fun postHabit(
        @Header("Authorization") token: String,
        @Body uidAndDate: LinkedTreeMap<String, Any>
      /*  @Body date: LinkedTreeMap<String, Int>*/)
}