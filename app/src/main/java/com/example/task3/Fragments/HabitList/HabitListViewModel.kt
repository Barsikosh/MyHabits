package com.example.task3.Fragments.HabitList

import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.task3.Habit
import com.example.task3.HabitData
import com.example.task3.MainActivity
import com.example.task3.R
import kotlin.collections.ArrayList


class HabitListViewModel(private val habitType: Habit.HabitType) : ViewModel(), Filterable {

    private val mutableHabit = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabit
    var habitsFilterList:  MutableLiveData<List<Habit>> = MutableLiveData()

    init {
        updateListHabits()
            habitsFilterList.value = habits.value
        HabitData.habits.observeForever( Observer { it.also { updateListHabits() } })
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val result = ( if (charSearch.isEmpty()) {
                    mutableHabit.value
                } else {
                    val resultList = ArrayList<Habit>()
                    habits.value!!.forEach {
                        if (it.name.startsWith(charSearch))
                            resultList.add(it)
                    }
                    resultList.toList()
                })
                val filterResults = FilterResults()
                filterResults.values = result
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                habitsFilterList.value = results?.values as List<Habit>?
            }
        }
    }

    fun getItems() = habits.value

    fun habitsMoved(startPosition: Int, nextPosition: Int) {
        val habits = mutableHabit.value as MutableList
        val habit = habits[startPosition]
        habits[startPosition] = habits[nextPosition]
        habits[nextPosition] = habit
    }

    fun habitDeleted(habit: Habit) {
        HabitData.removeItem(habit)
    }

    fun sortList(position: Int){
        when(position){
            0 -> mutableHabit.value = mutableHabit.value!!.sortedBy {el-> el.id }
            1 -> mutableHabit.value = mutableHabit.value!!.sortedBy {el-> el.time }
            2 -> mutableHabit.value = mutableHabit.value!!.sortedBy {el-> el.priority.value }
        }
    }

    private fun updateListHabits() {
        when (habitType) {
            Habit.HabitType.GOOD -> mutableHabit.value = HabitData.goodHabits
            Habit.HabitType.BAD -> mutableHabit.value = HabitData.badHabits
        }
    }

}