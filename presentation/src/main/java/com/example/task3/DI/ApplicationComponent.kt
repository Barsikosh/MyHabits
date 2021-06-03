package com.example.task3.DI


import android.content.Context
import com.example.domain.useCases.*
import com.example.task3.DI.ViewModelComponent.HabitListViewModelComponent
import com.example.task3.DI.ViewModelComponent.RedactorViewModelComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HabitsModule::class, ContextModule::class])
interface ApplicationComponent {

    fun getContext() : Context
    fun getAddHabitUseCase() : AddHabitUseCase
    fun getUpdateHabitUseCase() : UpdateHabitUseCase
    fun getDeleteHabitUseCase() : DeleteHabitUseCase
    fun getGetHabitsUseCase() : GetHabitsUseCase
    fun getPostHabitUseCase() : PostHabitUseCase
    fun getRedactorViewModelComponent(): RedactorViewModelComponent.Builder
    fun getListViewModelComponent(): HabitListViewModelComponent.Builder
}