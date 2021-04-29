package com.example.task3.Fragments.HabitRedactor

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import com.example.task3.DbRoom.HabitRepository
import com.example.task3.Habit
import com.example.task3.MainActivity
import com.example.task3.R
import kotlinx.android.synthetic.main.redactor_fragment.*

class RedactorHabitViewModel() : ViewModel() {

    private val HabitData = HabitRepository()

    private var mutableColor = MutableLiveData<Int>().apply {
        value = MainActivity.CONTEXT.resources.getColor(R.color.design_default_color_primary)
    }

    val color: LiveData<Int> = mutableColor
    var name = MutableLiveData<String>().apply { value = "" }
    var desription = MutableLiveData<String>().apply { value = "" }
    var type = MutableLiveData<Habit.HabitType>().apply { value = null }
    var priority = MutableLiveData<Habit.HabitPriority>().apply { value = Habit.HabitPriority.HIGH }

    val frequencyText : MutableLiveData<String> = MutableLiveData<String>()
    var frequency = MutableLiveData<Int>().apply {
        value = null
        observeForever {
            frequencyText.value = when (it) {
                null -> ""
                else -> it.toString()
            }
        }
    }

    val timesText : MutableLiveData<String> = MutableLiveData<String>()
    var times = MutableLiveData<Int>().apply {
        value = null
        observeForever {
            timesText.value = when (it) {
                null -> ""
                else -> it.toString()
            }
        }
    }




    fun addHabit(habit: Habit) {
        HabitData.addHabit(habit)
    }

    fun updateText(habit: Habit){
        mutableColor.value = habit.color
        name.value = habit.name
        desription.value = habit.description
        type.value = habit.type
        priority.value = habit.priority
    }


    fun saveNewData() {
        if (validation()) {
            val habit = collectHabit()
            HabitData.addHabit(habit)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun saveChangedData(habit: Habit) {
        if (validation()) {
            val newHabit = collectHabit()
            newHabit.id = habit.id
            HabitData.updateHabit(newHabit)
        }
    }


    private fun validation(): Boolean {
        var result = true

        if (type.value!! == null || name.value!!.isEmpty() || fr ) {
            result = false
        }

        if (name.value!!.isEmpty() || times.text.isEmpty()) {
            result = false
        }
        if (edit_habit_name.text.isEmpty()) {
            result = false
        }
        return result
    }

    private fun collectHabit(): Habit{
        return Habit(name.value!!, desription.value!!, type.value!!,priority.value!!,time.va
    }

    fun getPriority(position : Int){
        when (position){
            0 -> priority.value = Habit.HabitPriority.HIGH
            1 -> priority.value = Habit.HabitPriority.MEDIUM
            2 -> priority.value = Habit.HabitPriority.LOW
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun updateHabit(habit: Habit) {
        HabitData.updateHabit(habit)
    }
}