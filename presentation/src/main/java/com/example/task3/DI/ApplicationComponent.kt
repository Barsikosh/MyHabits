package com.example.task3.DI


import com.example.domain.useCases.*
import com.example.task3.DI.ViewModelComponent.HabitListViewModelComponent
import com.example.task3.DI.ViewModelComponent.ViewModelComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HabitsModule::class, ContextModule::class])
interface ApplicationComponent {

    fun getAddHabitUseCase() : AddHabitUseCase
    fun getUpdateHabitUseCase() : UpdateHabitUseCase
    fun getDeleteHabitUseCase() : DeleteHabitUseCase
    fun getGetHabitsUseCase() : GetHabitsUseCase
    fun getPostHabitUseCase() : PostHabitUseCase
    fun getViewModelComponent(): ViewModelComponent.Builder
    fun getListViewModelComponent(): HabitListViewModelComponent.Builder
}