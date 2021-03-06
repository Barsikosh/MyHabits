package com.example.task3

object HabitData{

    private var habits = mutableListOf<Habit>(
            Habit("Спать", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD"),
            Habit("Есть", "fdgreytrthgbv"),
            Habit("Пить воду :)", "gfdbttreter")
    )

    fun addHabit(habit: Habit){
        habits.add(habit)
    }

    fun updateHabit(habit: Habit, position: Int){
        if (position < getDataSize())
            habits[position] = habit
    }

    fun getDataSize() = habits.size

    fun getHabits() = habits

}