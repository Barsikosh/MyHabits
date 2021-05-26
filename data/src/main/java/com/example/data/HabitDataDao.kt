package com.example.data


import androidx.room.*
import com.example.domain.entities.Habit
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.Serializable


@Entity
@TypeConverters(
    HabitDataDao.TypeConverter::class,
    HabitDataDao.PriorityConverter::class,
    HabitDataDao.DatesConverter::class
)
data class HabitDataDao(
    @PrimaryKey
    @SerializedName("title")
    val name: String,
    val description: String,
    var type: Habit.HabitType,
    val priority: Habit.HabitPriority,
    val time: Int,
    @SerializedName("frequency") val period: Int,
    var color: Int
) : Serializable {

    @SerializedName("done_dates")
    var doneDates = mutableListOf<Int>()

    var uid: String? = null
    var date: Int = 0

    companion object {
        fun toDbDao(habit: Habit): HabitDataDao {
            val result = HabitDataDao(
                habit.name, habit.description, habit.type,
                habit.priority, habit.time, habit.period, habit.color
            )
            if (habit.uid != null)
                result.uid = habit.uid
            result.date = habit.date
            result.doneDates = habit.doneDates
            return result;
        }

        fun toHabit(habit: HabitDataDao): Habit {
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

    class DatesConverter {
        @androidx.room.TypeConverter
        fun fromDates(doneDates: MutableList<Int>): String {
            val gson = Gson()
            return gson.toJson(doneDates)
        }

        @androidx.room.TypeConverter
        fun toDates(datesString: String): MutableList<Int> {
            val listType = object : TypeToken<MutableList<Int>>() {}.type
            return Gson().fromJson(datesString, listType)
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
