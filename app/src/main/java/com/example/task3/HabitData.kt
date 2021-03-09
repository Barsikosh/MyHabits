package com.example.task3

object HabitData{

    private var habits = mutableListOf<Habit>()

    fun addHabit(habit: Habit){
        habits.add(habit)
    }

    fun updateHabit(habit: Habit, position: Int){
        if (position < getSize())
            habits[position] = habit
    }

    fun remove(position: Int){
        habits.removeAt(position)
    }

    fun getSize() = habits.size

    fun getHabit(position: Int) = habits[position]

    fun getHabits() = habits
}