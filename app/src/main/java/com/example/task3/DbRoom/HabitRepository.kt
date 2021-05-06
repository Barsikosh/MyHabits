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

class HabitRepository {

    companion object {
        var remoteHabits: List<Habit>? = null
        val dbHabits: LiveData<List<Habit>> =
            App.db.HabitDao().getAll()

        init {
            getRemoteHabit()
        }

        private fun getRemoteHabit() =
            GlobalScope.launch(Dispatchers.IO) {
                val response =
                    RepositoryProvider.provideRepository().getHabits()
                        .awaitResponse()
                remoteHabits = response.body()
                insertInDbRemoteHabits(remoteHabits)
            }

        private fun putHabit(habit: Habit) =
            GlobalScope.launch(Dispatchers.IO) {
                val response =
                    RepositoryProvider.provideRepository().putHabit(habit)
                        .awaitResponse()
                if (response.isSuccessful){
                    habit.uid = response.body()!!["uid"]
                    App.db.HabitDao().update(habit)
                }
            }

        private fun deleteHabitFromServer(habit: Habit) =
            GlobalScope.launch(Dispatchers.IO) {
                if (habit.uid != null) {
                    RepositoryProvider.provideRepository().deleteHabit(habit).awaitResponse()
                }
            }

        private fun insertInDbRemoteHabits(habits: List<Habit>?) {
            habits?.forEach {
                if (App.db.HabitDao().getById(it.uid) == null)
                    App.db.HabitDao().insert(it)
                else
                    App.db.HabitDao().update(it)
            }
        }
    }

    fun addHabit(habit: Habit) {
        val newId = App.db.HabitDao().insert(habit)
        habit.id = newId.toInt()
        putHabit(habit)
    }

    fun updateHabit(newHabit: Habit) {
        newHabit.date++
        App.db.HabitDao().update(newHabit)
        putHabit(newHabit)
    }

    fun removeItem(habit: Habit) {
        App.db.HabitDao().delete(habit)
        deleteHabitFromServer(habit)
    }
}