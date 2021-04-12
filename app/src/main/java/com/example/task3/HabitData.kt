package com.example.task3

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object HabitData : LiveData<Habit>() {

    private var mutableLiveData = MutableLiveData<LinkedHashMap<Long, Habit>>()

    val habits: LiveData<LinkedHashMap<Long, Habit>> = mutableLiveData

    init {
        habits.value = LinkedHashMap()
    }

    val goodHabits: List<Habit>
        get() = mutableLiveData.value?.filter { it -> it.value.type == Habit.HabitType.GOOD }?.values!!.toList()

    val badHabits: List<Habit>
        get() = mutableLiveData.value?.filter { it -> it.value.type == Habit.HabitType.BAD }?.values!!.toList()

    fun addHabit(habit: Habit) {
        val newId = mutableLiveData.value?.size?.toLong() ?: 1
        habit.id = newId
        mutableLiveData.value!![newId] = habit
    }

    fun updateHabit(newHabit: Habit, id: Long) {
        newHabit.id = id
        mutableLiveData.value!![id] = newHabit
    }

    fun removeItem(habit: Habit) {
        mutableLiveData.value!!.remove(habit.id)
    }
}