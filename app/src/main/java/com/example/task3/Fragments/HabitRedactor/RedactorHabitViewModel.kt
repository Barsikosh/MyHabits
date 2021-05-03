package com.example.task3.Fragments.HabitRedactor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.task3.DbRoom.HabitRepository
import com.example.task3.Habit
import com.example.task3.MainActivity
import com.example.task3.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RedactorHabitViewModel(): ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler{ _, e -> throw  e}

    var color: Int =  MainActivity.CONTEXT.resources.getColor(R.color.design_default_color_primary)
    private val repository = HabitRepository()

    fun addHabit(habit: Habit) = launch {
        withContext(Dispatchers.IO) {
            delay(1000)
            repository.addHabit(habit)}
    }

    fun updateHabit(habit: Habit) = launch {
        withContext(Dispatchers.IO) {repository.updateHabit(habit)}
    }
}