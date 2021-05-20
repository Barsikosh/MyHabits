package com.example.data

import com.example.domain.entities.Habit
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import kotlin.reflect.typeOf

class HabitTypeAdapter : TypeAdapter<HabitDbDao>() {

    override fun write(out: JsonWriter?, value: HabitDbDao?) {
        out!!.beginObject()
        out.name("color").value(value?.color ?: 0)
        out.name("count").value(value?.time ?: 0)
        out.name("date").value( value?.date)
        out.name("description").value(value?.description + " ")
        out.name("frequency").value(value?.period)
        out.name("priority").value(value?.priority!!.value ?: 0)
        out.name("title").value(value.name)
        out.name("type").value(value.type.value)
        out.name("done_dates").beginArray()
        value.doneDates.forEach{ out.value(it)}
        out.endArray()
        if (value.uid != null)
            out.name("uid").value(value.uid)
        out.endObject()
    }

    @ExperimentalStdlibApi
    override fun read(`in`: JsonReader?): HabitDbDao {
        var uid: String = ""
        var type: Int = 0
        var habitName: String = ""
        var description: String = ""
        var color: Int = 0
        var priority: Int = 0
        var frequency: Int = 0
        var count:Int = 0
        var date: Int = 0
        var done_dates = mutableListOf<Int>()
        `in`?.beginObject()
        while (`in`?.hasNext() == true) {
            val name = `in`.nextName()
            if (`in`.peek() == JsonToken.NULL) {
                `in`.nextNull()
                continue
            }

            when (name) {
                "uid" -> uid = `in`.nextString()
                "title" -> habitName = `in`.nextString()
                "description" -> description = `in`.nextString()
                "count" -> count = `in`.nextInt()
                "frequency" -> frequency = `in`.nextInt()
                "priority" -> priority = `in`.nextInt()
                "type" -> type = `in`.nextInt()
                "color" -> color = `in`.nextInt()
                "date" -> date = `in`.nextInt()
                "done_dates" -> {
                    `in`.beginArray()
                    while (`in`.peek() != JsonToken.END_ARRAY)
                        done_dates.add(`in`.nextInt())
                    `in`.endArray()
                }
            }
        }
        `in`?.endObject()
        val habit = HabitDbDao(habitName,description, Habit.HabitType.fromInt(type), Habit.HabitPriority.fromInt(priority), count,frequency,color)
        habit.uid = uid
        habit.date = date
        habit.doneDates = done_dates
        return habit
    }
}