package com.example.task3.Fragments.HabitList

import android.widget.Filter
import android.widget.Filterable
import androidx.lifecycle.*
import com.example.data.HabitDbDao
import com.example.data.HabitRepositoryImpl
import com.example.domain.entities.Habit
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.task3.DI.MyApplication
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class HabitListViewModel(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val habitType: Habit.HabitType
) : ViewModel(), Filterable, CoroutineScope {

    private val mutableHabit = MutableLiveData<List<Habit>>()
    val habits: LiveData<List<Habit>> = mutableHabit


    private var allMyHabits = mutableHabit.value

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + CoroutineExceptionHandler { _, e -> throw  e }

    private var observer: Observer<List<Habit>> = Observer<List<Habit>> {
        mutableHabit.value = it.filter { el -> el.type == habitType }
    }

    init {
        var da = getHabitsUseCase.getHabit().asLiveData()/*.observeForever(observer)*/
        var b = da.value
        da.observeForever(observer)
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
                    if (allMyHabits == null)
                        allMyHabits = mutableHabit.value!!

                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                mutableHabit.value = results?.values as List<Habit>?
            }
        }
    }

    override fun onCleared() {
        getHabitsUseCase.getHabit().asLiveData().removeObserver(observer)
        coroutineContext.cancelChildren()
    }

    fun habitAchieved(habit: Habit){

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