package com.example.task3.DbRoom

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.task3.Habit.Habit
import com.example.task3.Habit.RepositoryProvider
import com.example.task3.MyApplication
import kotlinx.coroutines.*
import retrofit2.HttpException

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
        MyApplication.db.HabitDao().insert(habit)
        //habit.id = newId.toInt()
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

    suspend fun removeFromDb(habit: Habit){
        MyApplication.db.HabitDao().delete(habit)
    }

    private suspend fun getRemoteHabit() {
        try{
            val response =
                RepositoryProvider.provideRepository().getHabits() // в новом потоке ?
            remoteHabits = response
            insertInDbRemoteHabits(remoteHabits)
        }
        catch (e:HttpException){
            Log.e("Http", "cant get remote data")
        }
    }

    private suspend fun putHabit(habit: Habit) {
        try{
            val response =
            RepositoryProvider.provideRepository().putHabit(habit)["uid"]
            habit.uid = response
            MyApplication.db.HabitDao().update(habit)
        }
        catch (e:HttpException){
            Log.e("HttpRequest", "did`nt put")
        }
    }

    private suspend fun updateRemoteData() {
        dbHabits.value?.forEach {
            if (it.uid == null)
                putHabit(it)
        }
    }

    private suspend fun deleteHabitFromServer(habit: Habit) {
        try {
            if (habit.uid != null) {
                RepositoryProvider.provideRepository().deleteHabit(habit)
            }
        }
       catch (e: HttpException){
           Log.e("HttpRequest", "did`nt delete")
       }
    }

    private fun insertInDbRemoteHabits(habits: List<Habit>?) {
        habits?.forEach {
                MyApplication.db.HabitDao().insert(it)
        }
    }
}