package com.example.data


import androidx.room.*
import com.example.domain.entities.Habit
import java.io.Serializable


@Entity
@TypeConverters(HabitDbDao.TypeConverter::class, HabitDbDao.PriorityConverter::class)
data class HabitDbDao(
    @PrimaryKey val name: String,
    val description: String,
    var type: Habit.HabitType,
    val priority: Habit.HabitPriority,
    val time: Int,
    val period: Int,
    var color: Int
) : Serializable {

    var doneDates = mutableListOf<Int>()

    var uid: String? = null
    var date: Int = 0

    companion object {
        fun toDbDao(habit: Habit): HabitDbDao {
            val result = HabitDbDao(
                habit.name, habit.description, habit.type,
                habit.priority, habit.time, habit.period, habit.color
            )
            if (habit.uid != null)
                result.uid = habit.uid
            result.date = habit.date
            result.doneDates = habit.doneDates
            return  result;
        }

        fun toHabit(habit: HabitDbDao): Habit {
            val result = Habit(
                habit.name, habit.description, habit.type,
                habit.priority, habit.time, habit.period, habit.color
            )
            result.date = habit.date
            result.uid = habit.uid
            result.doneDates = habit.doneDates
            return result
        }
    }

    class TypeConverter {
        @androidx.room.TypeConverter
        fun fromType(type: Habit.HabitType): String {
            return type.toString()
        }

        @androidx.room.TypeConverter
        fun toType(data: String): Habit.HabitType {
            return when (data) {
                "Вредная" -> Habit.HabitType.BAD
                else -> Habit.HabitType.GOOD
            }
        }
    }

    class PriorityConverter {
        @androidx.room.TypeConverter
        fun fromPriority(priority: Habit.HabitPriority): String {
            return priority.toString()
        }

        @androidx.room.TypeConverter
        fun toPriority(data: String): Habit.HabitPriority {
            return when (data) {
                "Высокий" -> Habit.HabitPriority.HIGH
                "Средний" -> Habit.HabitPriority.MEDIUM
                else -> Habit.HabitPriority.LOW
            }
        }
    }
}
