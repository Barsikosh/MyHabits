package com.example.task3.Fragments.HabitRedactor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.FrameMetricsAggregator
import androidx.lifecycle.ViewModel
import com.example.task3.Habit
import com.example.task3.HabitData
import com.example.task3.MainActivity
import com.example.task3.R

class RedactorHabitViewModel(): ViewModel() {

    var color: Int =  MainActivity.CONTEXT.resources.getColor(R.color.design_default_color_primary)

    fun addHabit(habit: Habit){
        HabitData.addHabit(habit)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateHabit(habit: Habit){
        HabitData.updateHabit(habit, habit.id)
    }
}