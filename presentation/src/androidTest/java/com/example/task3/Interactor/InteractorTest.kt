package com.example.task3.Fragments.HabitList

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.HabitDb
import com.example.data.HabitRepositoryImpl
import com.example.domain.entities.Habit
import com.example.domain.useCases.*
import com.example.task3.DI.HabitsModule
import com.example.task3.Fragments.HabitRedactor.RedactorHabitViewModelTest
import com.example.task3.MainCoroutineRule
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class InteractorTest : TestCase() {

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

    private lateinit var repository: HabitRepositoryImpl

    lateinit var getHabitsUseCase: GetHabitsUseCase

    lateinit var addHabitUseCase: AddHabitUseCase

    lateinit var deleteHabitUseCase: DeleteHabitUseCase

    lateinit var postHabitUseCase: PostHabitUseCase

    lateinit var updateHabitUseCase: UpdateHabitUseCase


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
        getHabitsUseCase = GetHabitsUseCase(repository, rule.testDispatcher)
        postHabitUseCase = PostHabitUseCase(repository, rule.testDispatcher)
        deleteHabitUseCase = DeleteHabitUseCase(repository, rule.testDispatcher)
        addHabitUseCase = AddHabitUseCase(repository, rule.testDispatcher)
        updateHabitUseCase = UpdateHabitUseCase(repository, rule.testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getHabitsTest() = rule.testDispatcher.runBlockingTest {
        addHabitUseCase.addHabit(habit)
        addHabitUseCase.addHabit(habit1)
        assertEquals(getHabitsUseCase.getHabit().first().size, 2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteHabitTest() = rule.testDispatcher.runBlockingTest {
        addHabitUseCase.addHabit(habit)
        deleteHabitUseCase.deleteHabit(habit)
        assertFalse(getHabitsUseCase.getHabit().first().contains(habit))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteHabitTest2() = runBlocking {

        val habit2 = Habit(
            "begat2",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )

        addHabitUseCase.addHabit(habit)
        addHabitUseCase.addHabit(habit1)
        addHabitUseCase.addHabit(habit2)
        deleteHabitUseCase.deleteHabit(habit)
        assertFalse(getHabitsUseCase.getHabit().first().contains(habit))
        assertTrue(getHabitsUseCase.getHabit().first().contains(habit2))
        assertTrue(getHabitsUseCase.getHabit().first().size == 2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testAddHabitUseCase() = rule.testDispatcher.runBlockingTest {
        val habit2 = Habit(
            "legat",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )
        addHabitUseCase.addHabit(RedactorHabitViewModelTest.habit)
        addHabitUseCase.addHabit(RedactorHabitViewModelTest.habit1)
        addHabitUseCase.addHabit(habit2)
        assertTrue(getHabitsUseCase.getHabit().first().size == 3)
        assertTrue(getHabitsUseCase.getHabit().first().contains(habit2))
        assertTrue(getHabitsUseCase.getHabit().first().contains(RedactorHabitViewModelTest.habit1))
        assertTrue(getHabitsUseCase.getHabit().first().contains(RedactorHabitViewModelTest.habit))
    }


    @ExperimentalCoroutinesApi
    @Test
    fun postHabitTest() = runBlocking {
        addHabitUseCase.addHabit(habit)
        postHabitUseCase.postHabit(habit, 5)
        assertTrue(habit.doneDates.size == 1)
        postHabitUseCase.postHabit(habit, 5)
        assertTrue(habit.doneDates.size == 2)

        /*for (i in 1..10) {
            postHabitUseCase.postHabit(habit, 5)
            assertTrue(habit.doneDates.size == i)
        }*/
    }
}
