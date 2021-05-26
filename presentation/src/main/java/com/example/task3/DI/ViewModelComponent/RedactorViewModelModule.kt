package com.example.task3.DI.ViewModelComponent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.useCases.AddHabitUseCase
import com.example.domain.useCases.UpdateHabitUseCase
import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment
import com.example.task3.Fragments.HabitRedactor.RedactorHabitViewModel
import dagger.Module
import dagger.Provides

@Module
class RedactorViewModelModule(private val fragment: HabitRedactorFragment) {

    @RedactorViewModelScope
    @Provides
    fun provideRedactorViewModel(
        addHabitUseCase: AddHabitUseCase,
        updateHabitUseCase: UpdateHabitUseCase
    ): RedactorHabitViewModel {

        return ViewModelProvider(fragment, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RedactorHabitViewModel(
                    addHabitUseCase,
                    updateHabitUseCase
                ) as T
            }
        }).get(RedactorHabitViewModel::class.java)
    }
}