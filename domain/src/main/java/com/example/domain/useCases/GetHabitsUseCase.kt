package com.example.domain.useCases

import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher

class GetHabitsUseCase(private val habitRepository : HabitRepository,
                       private val dispatcher : CoroutineDispatcher) {
    fun getHabit() = habitRepository.getLocalData()
}