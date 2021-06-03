package com.example.task3.Fragments.HabitList

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.HabitDb
import com.example.data.HabitRepositoryImpl
import com.example.domain.entities.Habit
import com.example.domain.useCases.DeleteHabitUseCase
import com.example.domain.useCases.GetHabitsUseCase
import com.example.domain.useCases.PostHabitUseCase
import com.example.task3.DI.HabitsModule
import com.example.task3.MainCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HabitListViewModelTest : TestCase() {

    companion object {
        val habit = Habit(
            "begat",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )

        val habit1 = Habit(
            "begat1",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )
    }

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    val rule = MainCoroutineRule()

    @get:Rule
    val inst = InstantTaskExecutorRule()

    private lateinit var viewModel: HabitListViewModel

    private lateinit var repository: HabitRepositoryImpl

    @ExperimentalCoroutinesApi
    @Before
    fun setup() {
        val module = HabitsModule()

        val apiRepository =
            module.provideApiRepository(module.provideHabitService(module.providesRetrofit()))

        val db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            HabitDb::class.java
        ).allowMainThreadQueries()
            .build()
        repository = HabitRepositoryImpl(db, apiRepository)
        val getHabitsUseCase = GetHabitsUseCase(repository, rule.testDispatcher)
        val postHabitUseCase = PostHabitUseCase(repository, rule.testDispatcher)
        val deleteHabitUseCase = DeleteHabitUseCase(repository, rule.testDispatcher)
        viewModel = HabitListViewModel(
            getHabitsUseCase,
            deleteHabitUseCase,
            postHabitUseCase,
            Habit.HabitType.GOOD
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getHabitsTest() = rule.testDispatcher.runBlockingTest {
        repository.addItem(habit)
        repository.addItem(habit1)
        assertEquals(viewModel.habits.value!!.size, 2)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun getHabitsTest2() = rule.testDispatcher.runBlockingTest {
        repository.addItem(habit)
        repository.addItem(habit1)
        repository.removeItem(habit)
        assertEquals(viewModel.habits.value!!.size, 1)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun deleteHabitTest() = rule.testDispatcher.runBlockingTest {
        repository.addItem(habit)
        repository.removeItem(habit)
        assertFalse(viewModel.habits.value!!.contains(habit))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteHabitTest2() = rule.testDispatcher.runBlockingTest {

        val habit2 = Habit(
            "begat2",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )

        repository.addItem(habit)
        repository.addItem(habit1)
        repository.addItem(habit2)
        repository.removeItem(habit)
        assertFalse(viewModel.habits.value!!.contains(habit))
        assertTrue(viewModel.habits.value!!.contains(habit1))
        assertTrue(viewModel.habits.value!!.size == 2)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun postHabitTest() = rule.testDispatcher.runBlockingTest {
        repository.addItem(habit)
        for (i in 1..10) {
            viewModel.postHabit(habit)
            assertTrue(habit.doneDates.size == i)
        }
    }
}
