package com.example.domain.useCases


import com.example.domain.entities.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UpdateHabitUseCase(
    private val habitDBRepository: HabitRepository,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun updateHabit(habit: Habit) {
        withContext(dispatcher) {
            habit.date++
            habitDBRepository.updateItem(habit)
        }
    }
}