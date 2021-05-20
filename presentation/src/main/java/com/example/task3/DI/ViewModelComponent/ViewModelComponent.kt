package com.example.task3.DI.ViewModelComponent

import com.example.task3.Fragments.HabitList.HabitListFragment
import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class ViewModelScope

@ViewModelScope
@Subcomponent(modules = [RedactorViewModelModule::class])
interface ViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun requestModule(module: RedactorViewModelModule?): Builder?
        fun build(): ViewModelComponent?
    }

    fun injectFragment2(habitRedactorFragment: HabitRedactorFragment)
}