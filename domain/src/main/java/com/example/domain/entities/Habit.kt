package com.example.domain.entities

import java.io.Serializable


data class Habit(
    val name: String,
    val description: String,
    val type: HabitType,
    val priority: HabitPriority,
    val time: Int,
    val period: Int,
    var color: Int
): Serializable  {

    var date:Int = 0
    var uid: String? = null

    var doneDates = mutableListOf<Int>()


    fun postDate(currentData: Int): Int{
        if (period == 0) return 0
        val lastUpdateDay = currentData - currentData.rem(this.period)
        return this.doneDates.filter { it >= lastUpdateDay }.size
    }


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

    enum class HabitPriority(val value: Int) {
        HIGH(2),
        MEDIUM(1),
        LOW(0);

        companion object {
            fun fromInt(value: Int) = values().first { it.value == value }
        }

        override fun toString(): String {
            return when (this) {
                HIGH -> /*MainActivity.CONTEXT.getString(R.string.high)*/ "Высокий"
                MEDIUM -> /*MainActivity.CONTEXT.getString(R.string.medium)*/ "Средний"
                LOW -> /*MainActivity.CONTEXT.getString(R.string.low)*/ "Низкий"
            }
        }
    }

}