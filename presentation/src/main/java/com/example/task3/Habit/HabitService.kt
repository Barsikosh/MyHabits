package com.example.task3.Habit


import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface HabitService {

    @GET("api/habit")
    suspend fun listHabits(@Header("Authorization") token: String): ArrayList<Habit>

    @PUT("api/habit")
    suspend fun putHabit(
        @Header("Authorization") token: String,
        @Body habit: Habit
    ): LinkedTreeMap<String, String>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    suspend fun deleteHabit(
        @Header("Authorization") token: String,
        @Body uid: LinkedTreeMap<String, String>
    )

    companion object Factory {

        fun create(): HabitService {
            val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
                val request: Request = chain.request()
                var response = chain.proceed(request)
                var tryCount = 0
                if (!(response.isSuccessful) && tryCount < 5) {
                    Thread.sleep(100)
                    tryCount++
                    response = chain.proceed(request)
                }
                response
            }
                .build()

            val gson = GsonBuilder()
                .registerTypeAdapter(Habit::class.java, HabitTypeAdapter())
                .create()
            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://droid-test-server.doubletapp.ru/")
                .build()

            return retrofit.create(HabitService::class.java)
        }
    }
}