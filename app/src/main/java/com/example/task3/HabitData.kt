package com.example.task3

object HabitData {

    private var habits = mutableListOf<Habit>()

    val goodHabits = mutableListOf<Habit>()

    val badHabits = mutableListOf<Habit>()

    init {
        addHabit(Habit("Pit", "fdsf", Habit.HabitType.BAD, Habit.HabitPriority.HIGH, 2,2))
    }
    fun addHabit(habit: Habit) {
        habits.add(habit)
        when (habit.type) {
            Habit.HabitType.GOOD -> goodHabits.add(habit)
            Habit.HabitType.BAD -> badHabits.add(habit)
        }
    }

    fun updateHabit(habit: Habit, position: Int) {
        if (position < size)
            habits[position] = habit
    }

    fun remove(position: Int) {
        habits.removeAt(position)
    }


    val size = habits.size

    fun getHabit(position: Int): Habit = habits[position]

    fun getHabits() = habits
}