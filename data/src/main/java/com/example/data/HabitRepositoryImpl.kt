package com.example.data

import android.util.Log
import com.example.domain.entities.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class HabitRepositoryImpl(
    private val dataBase: HabitDb,
    private val retrofitService: ApiRepository
) : HabitRepository {

    private var remoteHabits: List<HabitDataDao>? = null

    private val dbHabits = dataBase.HabitDao().getAll()

    init {
        GlobalScope.launch(Dispatchers.IO) {
            getRemoteHabit()
        }
    }

    override suspend fun addItem(habit: Habit) {
        val dbHabit = HabitDataDao.toDbDao(habit)
        dataBase.HabitDao().insert(dbHabit)
        putHabit(dbHabit)
    }

    override fun getLocalData(): Flow<List<Habit>> {
        return dbHabits.map { el ->
            el.map { HabitDataDao.toHabit(it) }
        }
    }

    override suspend fun updateItem(newHabit: Habit) {
        val dbHabit = HabitDataDao.toDbDao(newHabit)
        dataBase.HabitDao().update(dbHabit)
        putHabit(dbHabit)
    }

    override suspend fun removeItem(habit: Habit) {
        val dbHabit = HabitDataDao.toDbDao(habit)
        dataBase.HabitDao().delete(dbHabit)
        deleteHabitFromServer(dbHabit)
    }

    override suspend fun removeFromDb(habit: Habit) {
        val dbHabit = HabitDataDao.toDbDao(habit)
        dataBase.HabitDao().delete(dbHabit)
    }

    override suspend fun postItem(habit: Habit) {
        val dbHabit = HabitDataDao.toDbDao(habit)
        remotePostHabit(dbHabit)
        dataBase.HabitDao().update(dbHabit)
    }

    private suspend fun remotePostHabit(habit: HabitDataDao) {
        try {
            retrofitService.postHabit(habit)
        } catch (e: retrofit2.HttpException) {
            Log.e("HttpRequest", "did`nt post")
        }
        catch (e: UnknownHostException){
            Log.e("connection","UnknownHostException")
        }
    }

    private suspend fun getRemoteHabit() {
        try {
            val response =
                retrofitService.getHabits()
            remoteHabits = response
            insertInDbRemoteHabits(remoteHabits)
        } catch (e: retrofit2.HttpException) {
            Log.e("Http", "cant get remote data")
        }
        catch (e: UnknownHostException){
            Log.e("connection","UnknownHostException")
        }
    }

    private suspend fun putHabit(habit: HabitDataDao) {
        try {
            val response =
                this.retrofitService.putHabit(habit)["uid"]
            habit.uid = response
            dataBase.HabitDao().update(habit)
        } catch (e: retrofit2.HttpException) {
            Log.e("HttpRequest", "did`nt put")
        }
        catch (e: UnknownHostException){
            Log.e("connection","UnknownHostException")
        }
    }

    private suspend fun deleteHabitFromServer(habit: HabitDataDao) {
        try {
            if (habit.uid != null) {
                this.retrofitService.deleteHabit(habit)
            }
        } catch (e: retrofit2.HttpException) {
            Log.e("HttpRequest", "did`nt delete")
        }
        catch (e: UnknownHostException){
            Log.e("connection","UnknownHostException")
        }
    }

    private fun insertInDbRemoteHabits(habits: List<HabitDataDao>?) {
        habits?.forEach {
            this.dataBase.HabitDao().insert(it)
        }
    }
}

