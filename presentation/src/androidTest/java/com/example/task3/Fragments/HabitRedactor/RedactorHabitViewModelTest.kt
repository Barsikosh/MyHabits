package com.example.task3.Fragments.HabitRedactor

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.HabitDb
import com.example.data.HabitRepositoryImpl
import com.example.domain.entities.Habit
import com.example.domain.useCases.*
import com.example.task3.DI.HabitsModule
import com.example.task3.MainCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class RedactorHabitViewModelTest : TestCase() {

    companion object{
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

    private lateinit var viewModel: RedactorHabitViewModel

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
        val addHabitUseCase = AddHabitUseCase(repository, rule.testDispatcher)
        val updateHabitUseCase = UpdateHabitUseCase(repository, rule.testDispatcher)
        viewModel = RedactorHabitViewModel(
            addHabitUseCase,
            updateHabitUseCase
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testAddHabit() = rule.testDispatcher.runBlockingTest {
        viewModel.addHabit(habit)
        assertTrue(repository.getLocalData().asLiveData().value!!.size == 1)
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testAddHabit2() = rule.testDispatcher.runBlockingTest {
        val habit2 = Habit(
            "legat",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )

        viewModel.addHabit(habit)
        viewModel.addHabit(habit1)
        viewModel.addHabit(habit2)
        assertTrue(repository.getLocalData().first().size == 3)
        assertTrue(repository.getLocalData().first().contains(habit2))
        assertTrue(repository.getLocalData().first().contains(habit1))
        assertTrue(repository.getLocalData().first().contains(habit))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testUpdateHabit() = rule.testDispatcher.runBlockingTest {
        viewModel.addHabit(habit)
        habit.description = "lela"
        viewModel.updateHabit(habit)
        assertTrue(repository.getLocalData().asLiveData().value!!.size == 1)
        assertTrue(repository.getLocalData().asLiveData().value!![0].description == "lela")
    }
}