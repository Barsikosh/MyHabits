package com.example.domain.useCases

import com.example.domain.entities.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PostHabitUseCase(
    private val habitRepository: HabitRepository,
    private val dispatcher: CoroutineDispatcher
) {
    suspend fun postHabit(habit: Habit, todayTime: Int) {
        withContext(dispatcher) {
            habit.doneDates.add(todayTime)
            habitRepository.postItem(habit)
        }
    }
}