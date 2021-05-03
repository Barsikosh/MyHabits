package com.example.task3.Fragments.HabitList

import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.task3.Habit
import com.example.task3.DbRoom.HabitRepository
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class HabitListViewModel(var habitType: Habit.HabitType) : ViewModel(), Filterable, CoroutineScope {

    private val mutableHabit = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabit
    private var myHabitData: HabitRepository = HabitRepository()

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw  e }


    init {
        onCreate()
    }

    private lateinit var observer: Observer<List<Habit>>


    private fun onCreate() {
        observer = Observer<List<Habit>> { it ->
            mutableHabit.value = it.filter { el -> el.type == habitType }
        }
        myHabitData.habits.observeForever(observer)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val filterResults = FilterResults()
                if (charSearch.isEmpty())
                    filterResults.values = mutableHabit.value
                filterResults.values =
                    mutableHabit.value!!.filter { it.name.startsWith(charSearch) }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mutableHabit.value = results?.values as? List<Habit>?
            }
        }
    }

    override fun onCleared() {
        myHabitData.habits.removeObserver(observer)
        coroutineContext.cancelChildren()
    }

    fun getItems() = habits.value

    fun habitsMoved(startPosition: Int, nextPosition: Int) {
        val habits = mutableHabit.value as MutableList
        val habit = habits[startPosition]
        habits[startPosition] = habits[nextPosition]
        habits[nextPosition] = habit
    }

    fun deleteHabit(habit: Habit) = launch {
        withContext(Dispatchers.Default) { myHabitData.removeItem(habit) }
    }

    fun sortList(position: Int) {
        when (position) {
            0 -> mutableHabit.value = mutableHabit.value!!.sortedBy { el -> el.id }
            1 -> mutableHabit.value = mutableHabit.value!!.sortedBy { el -> el.time * el.period }
            2 -> mutableHabit.value = mutableHabit.value!!.sortedBy { el -> el.priority.value }
        }
    }
}