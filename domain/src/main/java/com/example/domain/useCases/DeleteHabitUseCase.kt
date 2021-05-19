package com.example.domain.useCases


import com.example.domain.entities.Habit
import com.example.domain.repository.HabitRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DeleteHabitUseCase (private val habitDBRepository: HabitRepository,
                          private val dispatcher : CoroutineDispatcher
) {

    suspend fun deleteHabit(habit: Habit){
        withContext(dispatcher){
            habitDBRepository.removeItem(habit)
        }
    }
}