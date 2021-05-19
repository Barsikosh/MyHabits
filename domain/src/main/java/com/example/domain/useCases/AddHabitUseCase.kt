package com.example.domain.useCases

import com.example.domain.entities.Habit

import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AddHabitUseCase(private val habitRepository: HabitRepository,
                      private val dispatcher : CoroutineDispatcher) {
    suspend fun addHabit(habit: Habit){
        withContext(dispatcher){
            habitRepository.addItem(habit)
        }
    }
}