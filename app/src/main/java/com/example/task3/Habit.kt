package com.example.task3

import java.io.FileDescriptor
import java.io.Serializable


data class Habit(val name: String, val description: String, val type: HabitType,
                 val priority: HabitPriority, val time: Int, val period: Int)
    : Serializable {

    enum class HabitType(val value: Int) {
        GOOD(0),
        BAD(1);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }

        override fun toString(): String {
            return if (this == BAD)
                "Вредная"
            else "Полезная"
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
                HIGH -> "Приоритет: Высокий"
                MEDIUM -> "Приоритет: Средний"
                LOW -> "Приоритет: Низкий"
            }
        }
    }

}