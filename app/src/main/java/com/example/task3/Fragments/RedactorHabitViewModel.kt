package com.example.task3.Fragments

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.task3.Habit
import com.example.task3.HabitData

class RedactorHabitViewModel: ViewModel() {

    fun addHabit(habit: Habit){
        HabitData.addHabit(habit)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateHabit(habit: Habit){
        HabitData.updateHabit(habit, habit.id)
    }
}