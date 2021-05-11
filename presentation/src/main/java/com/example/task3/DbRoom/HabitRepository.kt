package com.example.task3.DbRoom

import androidx.lifecycle.LiveData
import com.example.task3.Habit.Habit
import com.example.task3.Habit.RepositoryProvider
import com.example.task3.MyApplication
import kotlinx.coroutines.*

object HabitRepository {

    var remoteHabits: List<Habit>? = null
    val dbHabits: LiveData<List<Habit>> =
        MyApplication.db.HabitDao().getAll()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            updateRemoteData()
            getRemoteHabit()
        }
    }

    suspend fun addItem(habit: Habit) {
        val newId = MyApplication.db.HabitDao().insert(habit)
        habit.id = newId.toInt()
        putHabit(habit)
    }

    suspend fun updateItem(newHabit: Habit) {
        newHabit.date++
        MyApplication.db.HabitDao().update(newHabit)
        putHabit(newHabit)
    }

    suspend fun removeItem(habit: Habit) {
        MyApplication.db.HabitDao().delete(habit)
        deleteHabitFromServer(habit)
    }

    private suspend fun getRemoteHabit() {
        val response =
            RepositoryProvider.provideRepository().getHabits() // в новом потоке ?
        remoteHabits = response
        insertInDbRemoteHabits(remoteHabits)
    }

    private suspend fun putHabit(habit: Habit) {
        val response =
            RepositoryProvider.provideRepository().putHabit(habit)
        habit.uid = response["uid"]
        MyApplication.db.HabitDao().update(habit)
    }

    private suspend fun updateRemoteData() {
        dbHabits.value?.forEach {
            if (it.uid == null)
                putHabit(it)
        }
    }

    private suspend fun deleteHabitFromServer(habit: Habit) {
        if (habit.uid != null) {
            RepositoryProvider.provideRepository().deleteHabit(habit)
        }
    }

    private fun insertInDbRemoteHabits(habits: List<Habit>?) {
        habits?.forEach {
            MyApplication.db.HabitDao().insert(it)
        }
    }
}