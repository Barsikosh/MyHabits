package com.example.task3

object HabitData {

    private var habits = mutableMapOf<Long, Habit>()

    val goodHabits = mutableListOf<Habit>()

    val badHabits = mutableListOf<Habit>()

    val size get() = habits.size

    init {
        addHabit(Habit(0, "Pit", "fdsf", Habit.HabitType.BAD, Habit.HabitPriority.HIGH, 2, 2))
        addHabit(
            Habit(
                1,
                "Pifdsfsdt",
                "fdsf",
                Habit.HabitType.GOOD,
                Habit.HabitPriority.HIGH,
                2,
                2
            )
        )
    }

    fun getNewOrUpdatedId(id: Long?): Long {
        return id ?: habits.size.toLong()
    }

    fun addHabit(habit: Habit) {
        habits[getNewOrUpdatedId(null)] = habit
        when (habit.type) {
            Habit.HabitType.GOOD -> goodHabits.add(habit)
            Habit.HabitType.BAD -> badHabits.add(habit)
        }
    }

    fun updateHabit(newHabit: Habit, id: Long) {
        newHabit.id = id
        if (habits[id]!!.type != newHabit.type) {
            habits[id] = newHabit
            when (newHabit.type) {
                Habit.HabitType.GOOD -> {
                    badHabits.removeIf { it.id == id }
                    goodHabits.add(newHabit)
                }
                Habit.HabitType.BAD -> {
                    goodHabits.removeIf { it.id == id }
                    badHabits.add(newHabit)
                }
            }

        } else {
            habits[id] = newHabit
            when (newHabit.type) {
                Habit.HabitType.GOOD -> goodHabits[goodHabits.indexOfFirst { it.id == id }] =
                    newHabit
                Habit.HabitType.BAD -> badHabits[badHabits.indexOfFirst { it.id == id }] = newHabit
            }
        }
    }

    fun remove(id: Long) {
        habits.remove(id)
    }

    fun getHabit(id: Long): Habit? = habits[id]

    fun getHabits() = habits
}