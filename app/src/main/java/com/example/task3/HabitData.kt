package com.example.task3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object HabitData : LiveData<Habit>() {
    private val mutableHabits = MutableLiveData<LinkedHashMap<Long, Habit>>()
    var habits: LiveData<LinkedHashMap<Long, Habit>> = mutableHabits

    init {
        mutableHabits.value = LinkedHashMap()
    }

    val goodHabits: List<Habit>
        get() = habits.value!!.filter { it -> it.value.type == Habit.HabitType.GOOD }.values.toList()

    val badHabits: List<Habit>
        get() = habits.value!!.filter { it -> it.value.type == Habit.HabitType.BAD }.values.toList()

    fun addHabit(habit: Habit) {
        val newId = habits.value!!.size.toLong()
        habit.id = newId
        mutableHabits.value!![newId] = habit
    }

    fun updateHabit(newHabit: Habit, id: Long) {
        newHabit.id = id
        mutableHabits.value!![id] = newHabit
    }

    fun removeItem(habit: Habit) {
        mutableHabits.value = mutableHabits.value!!.also{ it.remove(habit.id)}
    }
}