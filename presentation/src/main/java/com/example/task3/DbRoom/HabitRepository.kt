package com.example.task3.DbRoom

import androidx.lifecycle.LiveData
import com.example.task3.Habit.Habit
import com.example.task3.Habit.RepositoryProvider
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory

object HabitRepository {

    var remoteHabits: List<Habit>? = null
    val dbHabits: LiveData<List<Habit>> =
        App.db.HabitDao().getAll()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            getRemoteHabit()
        }
    }

    suspend fun addHabit(habit: Habit) {
        val newId = App.db.HabitDao().insert(habit)
        habit.id = newId.toInt()
        putHabit(habit)
    }

    suspend fun updateHabit(newHabit: Habit) {
        newHabit.date++
        App.db.HabitDao().update(newHabit)
        putHabit(newHabit)
    }

    suspend fun removeItem(habit: Habit) {
        App.db.HabitDao().delete(habit)
        deleteHabitFromServer(habit)
    }

    private suspend fun getRemoteHabit() {
        val response =
            RepositoryProvider.provideRepository().getHabits()
        remoteHabits = response
        insertInDbRemoteHabits(remoteHabits)
    }

    private suspend fun putHabit(habit: Habit) {
        val response =
            RepositoryProvider.provideRepository().putHabit(habit)
        habit.uid = response["uid"]
        App.db.HabitDao().update(habit)
    }


    private suspend fun deleteHabitFromServer(habit: Habit) {
        if (habit.uid != null) {
            RepositoryProvider.provideRepository().deleteHabit(habit)
        }
    }

    private fun insertInDbRemoteHabits(habits: List<Habit>?) {
        habits?.forEach {
            App.db.HabitDao().insert(it)
        }
    }
}