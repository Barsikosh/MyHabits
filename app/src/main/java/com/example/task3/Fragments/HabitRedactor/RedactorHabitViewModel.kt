package com.example.task3.Fragments.HabitRedactor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.task3.DbRoom.HabitRepository
import com.example.task3.Habit
import com.example.task3.MainActivity
import com.example.task3.R

class RedactorHabitViewModel(): ViewModel() {

    var color: Int =  MainActivity.CONTEXT.resources.getColor(R.color.design_default_color_primary)
    private val HabitData = HabitRepository()

    fun addHabit(habit: Habit){
        HabitData.addHabit(habit)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateHabit(habit: Habit){
        HabitData.updateHabit(habit)
    }
}