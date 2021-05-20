package com.example.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.bumptech.glide.load.HttpException
import com.example.domain.entities.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


class HabitRepositoryImpl(
    private val dataBase: HabitDb,
    private val retrofitService: ApiRepository
) : HabitRepository {

    var remoteHabits: List<HabitDbDao>? = null

    val dbHabits = dataBase.HabitDao().getAll()


    init {
        GlobalScope.launch(Dispatchers.IO) {
            //updateRemoteData()
            getRemoteHabit()
        }
    }


    override suspend fun addItem(habit: Habit) {
        val dbHabit = HabitDbDao.toDbDao(habit)
        dataBase.HabitDao().insert(dbHabit)
        putHabit(dbHabit)
    }

    override fun getLocalData(): Flow<List<Habit>> {
        return dataBase.HabitDao().getAll().map { el ->
            el.map { HabitDbDao.toHabit(it) }
        }
    }

    override suspend fun updateItem(newHabit: Habit) {
        val dbHabit = HabitDbDao.toDbDao(newHabit)
        dbHabit.date++
        dataBase.HabitDao().update(dbHabit)
        putHabit(dbHabit)
    }

    override suspend fun removeItem(habit: Habit) {
        val dbHabit = HabitDbDao.toDbDao(habit)
        dataBase.HabitDao().delete(dbHabit)
        deleteHabitFromServer(dbHabit)
    }

    override suspend fun removeFromDb(habit: Habit) {
        val dbHabit = HabitDbDao.toDbDao(habit)
        dataBase.HabitDao().delete(dbHabit)
    }

    override suspend fun postItem(habit: Habit) {
        val dbHabit = HabitDbDao.toDbDao(habit)
        putHabit(dbHabit)
        dataBase.HabitDao().update(dbHabit)

    }

    private suspend fun postHabit(habit: HabitDbDao){
        try {
            retrofitService.postHabit(habit)
        } catch (e: retrofit2.HttpException) {
            Log.e("HttpRequest", "did`nt post")
        }
    }

    override fun getRemoteData(): List<Habit>? = remoteHabits?.map { el -> HabitDbDao.toHabit(el) }

    private suspend fun getRemoteHabit() {
        try {
            val response =
                retrofitService.getHabits()
            remoteHabits = response
            insertInDbRemoteHabits(remoteHabits)
        } catch (e: retrofit2.HttpException) {
            Log.e("Http", "cant get remote data")
        }
    }

    private suspend fun putHabit(habit: HabitDbDao) {
        try {
            val response =
                this.retrofitService.putHabit(habit)["uid"]
            habit.uid = response
            dataBase.HabitDao().update(habit)
        } catch (e: retrofit2.HttpException) {
            Log.e("HttpRequest", "did`nt put")
        }
    }

    private suspend fun updateRemoteData() {
        dbHabits.first().forEach {
            if (it.uid == null)
                putHabit(it)
        }
    }

    private suspend fun deleteHabitFromServer(habit: HabitDbDao) {
        try {
            if (habit.uid != null) {
                this.retrofitService.deleteHabit(habit)
            }
        } catch (e: retrofit2.HttpException) {
            Log.e("HttpRequest", "did`nt delete")
        }
    }

    private fun insertInDbRemoteHabits(habits: List<HabitDbDao>?) {
        habits?.forEach {
            this.dataBase.HabitDao().insert(it)
        }
    }


}

