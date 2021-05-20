package com.example.task3.DI.ViewModelComponent

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.entities.Habit
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.domain.useCases.PostHabitUseCase
import com.example.task3.Fragments.HabitList.HabitListFragment
import com.example.task3.Fragments.HabitList.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
class ListViewModelModule(private val habitListFragment: HabitListFragment, private val habitType: Habit.HabitType) {

    @ViewModelScope
    @Provides
    fun provideHabitListViewModel(
        habitsUseCase: GetHabitsUseCase,
        deleteHabitUseCase: DeleteHabitUseCase,
        postHabitUseCase: PostHabitUseCase
    ): HabitListViewModel {

        return ViewModelProvider(habitListFragment, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel(
                    habitsUseCase,
                    deleteHabitUseCase,
                    postHabitUseCase,
                    habitType
                ) as T
            }
        }).get(HabitListViewModel::class.java)

    }
}