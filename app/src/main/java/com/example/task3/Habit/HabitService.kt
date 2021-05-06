package com.example.task3.Habit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface HabitService {
    @GET("api/habit")
    fun listHabits(@Header("Authorization") token: String): Call<ArrayList<Habit>>

    @PUT("api/habit")
    fun putHabit(@Header("Authorization") token: String, @Body habit: Habit): Call<LinkedTreeMap<String,String>>

    @HTTP(method = "DELETE", path = "api/habit", hasBody = true)
    fun deleteHabit(@Header("Authorization") token: String, @Body uid: LinkedTreeMap<String,String>): Call<Void>

    companion object Factory {

        fun create(): HabitService {
            val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
                val request: Request = chain.request()
                var response = chain.proceed(request)
                var tryCount = 0
                if (!(response.isSuccessful) && tryCount < 5) {
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