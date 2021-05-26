package com.example.task3.Fragments.HabitRedactor

import androidx.lifecycle.ViewModel
import com.example.domain.entities.Habit
import com.example.domain.useCases.AddHabitUseCase
import com.example.domain.useCases.UpdateHabitUseCase
import com.example.task3.MainActivity
import com.example.task3.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RedactorHabitViewModel(private val addHabitUseCase: AddHabitUseCase,
                             private val updateHabitUseCase: UpdateHabitUseCase
): ViewModel(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw  e }

    var color: Int = MainActivity.CONTEXT.resources.getColor(R.color.design_default_color_primary)

    fun addHabit(habit: Habit) = launch {
        addHabitUseCase.addHabit(habit)
    }

    fun updateHabit(habit: Habit) = launch {
        updateHabitUseCase.updateHabit(habit)
    }
}