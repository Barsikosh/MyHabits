package com.example.task3.DI


import com.example.domain.useCases.AddHabitUseCase
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.domain.useCases.UpdateHabitUseCase
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [HabitsModule::class, ContextModule::class])
interface ApplicationComponent {

    fun getAddHabitUseCase() : AddHabitUseCase
    fun getUpdateHabitUseCase() : UpdateHabitUseCase
    fun getDeleteHabitUseCase() : DeleteHabitUseCase
    fun getGetHabitsUseCase() : GetHabitsUseCase

}