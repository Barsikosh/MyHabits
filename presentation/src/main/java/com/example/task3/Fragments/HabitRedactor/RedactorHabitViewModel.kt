package com.example.task3.Fragments.HabitRedactor

import androidx.lifecycle.ViewModel
import com.example.task3.DbRoom.HabitRepository
import com.example.task3.Habit.Habit
import com.example.task3.MainActivity
import com.example.task3.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RedactorHabitViewModel() : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw  e }

    var color: Int = MainActivity.CONTEXT.resources.getColor(R.color.design_default_color_primary)

    fun addHabit(habit: Habit) = launch(Dispatchers.IO) {
        HabitRepository.addItem(habit)
    }

    fun updateHabit(habit: Habit) = launch(Dispatchers.IO) {
        HabitRepository.updateItem(habit)
    }

    fun removeHabitFromDb(habit: Habit) = launch(Dispatchers.IO) {
        HabitRepository.removeFromDb(habit)
    }
}