package com.example.domain.useCases

import com.example.domain.entities.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class HabitAchieved(private val habitRepository: HabitRepository,
                      private val dispatcher : CoroutineDispatcher
) {
    suspend fun habitAchieved(habit: Habit){
        withContext(dispatcher){
            habitRepository.updateItem(habit)
        }
    }
}