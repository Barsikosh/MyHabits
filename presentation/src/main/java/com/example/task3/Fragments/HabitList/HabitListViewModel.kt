package com.example.task3.Fragments.HabitList

import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.domain.entities.Habit
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.domain.useCases.PostHabitUseCase
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class HabitListViewModel(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val postHabitUseCase: PostHabitUseCase,
    private val habitType: Habit.HabitType
) : ViewModel(), Filterable, CoroutineScope {

    private val mutableHabit = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabit

    private val todayTime = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    private var allMyHabits = mutableHabit.value

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw  e }

    private var observer: Observer<List<Habit>> = Observer<List<Habit>> {
        mutableHabit.value = it.filter { el -> el.type == habitType }
    }

    init {
        getHabitsUseCase.getHabit().asLiveData().observeForever(observer)
        allMyHabits = mutableHabit.value
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                val filterResults = FilterResults()

                if (charSearch.isEmpty())
                    filterResults.values = allMyHabits
                else {
                    filterResults.values =
                        mutableHabit.value!!.filter { it.name.startsWith(charSearch) }
                    if (allMyHabits == null) {
                        filterResults.values =
                            mutableHabit.value!!.filter { it.name.startsWith(charSearch) }
                        allMyHabits = mutableHabit.value!!
                    } else {
                        filterResults.values =
                            allMyHabits!!.filter { it.name.startsWith(charSearch) }
                    }

                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mutableHabit.value = results?.values as List<Habit>? ?: ArrayList<Habit>()
            }
        }
    }

    override fun onCleared() {
        getHabitsUseCase.getHabit().asLiveData().removeObserver(observer)
        coroutineContext.cancelChildren()
    }

    fun postHabit(habit: Habit) = launch {
        habit.date = todayTime
        postHabitUseCase.postHabit(habit, todayTime)
    }

    fun getItems() = getHabitsUseCase.getHabit().asLiveData().value

    fun habitsMoved(startPosition: Int, nextPosition: Int) {
        val habits = mutableHabit.value as MutableList
        val habit = habits[startPosition]
        habits[startPosition] = habits[nextPosition]
        habits[nextPosition] = habit
    }

    fun deleteHabit(habit: Habit) = launch(Dispatchers.IO) {
        deleteHabitUseCase.deleteHabit(habit)
    }

    fun sortList(position: Int) = launch(Dispatchers.Default) {
        when (position) {
            0 -> mutableHabit.postValue(mutableHabit.value!!.sortedBy { el -> el.date })
            1 -> mutableHabit.postValue(mutableHabit.value!!.sortedBy { el -> el.time * el.period })
            2 -> mutableHabit.postValue(mutableHabit.value!!.sortedBy { el -> el.priority.value })
        }
    }
}