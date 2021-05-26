package com.example.task3.DI.ViewModelComponent

import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class RedactorViewModelScope

@RedactorViewModelScope
@Subcomponent(modules = [RedactorViewModelModule::class])
interface RedactorViewModelComponent {

    @Subcomponent.Builder
    interface Builder {
        fun requestModule(module: RedactorViewModelModule?): Builder?
        fun build(): RedactorViewModelComponent?
    }

    fun injectRedactorFragment(habitRedactorFragment: HabitRedactorFragment)
}