package com.example.task3.DI.ViewModelComponent

import com.example.task3.Fragments.HabitList.HabitListFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class ListViewModelScope

@ListViewModelScope
@Subcomponent(modules = [ListViewModelModule::class])
interface HabitListViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun requestModule(module: ListViewModelModule?): Builder?
        fun build(): HabitListViewModelComponent?
    }

    fun injectListFragment(habitListFragment: HabitListFragment)
}