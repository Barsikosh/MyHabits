package com.example.task3


import java.io.Serializable
import kotlin.coroutines.coroutineContext


data class Habit(var id: Long, val name: String, val description: String, val type: HabitType,
                 val priority: HabitPriority, val time: Int, val period: Int, var color: Int)
    : Serializable {

    enum class HabitType(val value: Int) {
        GOOD(0),
        BAD(1);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }

        override fun toString(): String {
            return if (this == BAD)
                MainActivity.CONTEXT.getString(R.string.bad_habit)
            else MainActivity.CONTEXT.getString(R.string.good_habit)
        }
    }

    enum class HabitPriority(val value: Int){
        HIGH(0),
        MEDIUM(2),
        LOW(1);
        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }

        override fun toString(): String {
            return when(this){
                HIGH -> MainActivity.CONTEXT.getString(R.string.high)
                MEDIUM -> MainActivity.CONTEXT.getString(R.string.medium)
                LOW -> MainActivity.CONTEXT.getString(R.string.low)
            }
        }
    }

}