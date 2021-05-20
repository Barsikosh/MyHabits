package com.example.task3.DI.ViewModelComponent

import com.example.task3.Fragments.HabitList.HabitListFragment
import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment
import dagger.Component
import dagger.Subcomponent


@ViewModelScope
@Subcomponent(modules = [ListViewModelModule::class])
interface HabitListViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun requestModule(module: ListViewModelModule?): Builder?
        fun build(): HabitListViewModelComponent?
    }

    fun injectFragment(habitListFragment: HabitListFragment)
}