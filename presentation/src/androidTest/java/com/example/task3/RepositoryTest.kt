package com.example.task3

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.HabitDb
import com.example.data.HabitRepositoryImpl
import com.example.domain.entities.Habit
import com.example.task3.DI.HabitsModule
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest : TestCase() {

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
    }


    @ExperimentalCoroutinesApi
    @Test
    fun testAdd2() = rule.testDispatcher.runBlockingTest {
        repository.addItem(habit)
        repository.addItem(habit1)
        assertTrue(repository.getLocalData().first().size == 2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testDelete() = rule.testDispatcher.runBlockingTest {
        repository.addItem(habit)
        repository.addItem(habit1)
        repository.removeItem(habit)
        assertTrue(repository.getLocalData().first().size == 1)
        assertFalse(repository.getLocalData().first().contains(habit))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testUpdate() = rule.testDispatcher.runBlockingTest {
        val habit2 = Habit(
            "legat",
            "lala",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )
        repository.addItem(habit2)
        repository.addItem(habit1)
        habit2.description = "lele"
        repository.updateItem(habit2)
        assertTrue(repository.getLocalData().first().size == 2)
        assertTrue(repository.getLocalData().first().first { el -> el.name == habit2.name }.description == "lele")
        repository.updateItem(habit1)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testPutSameNameObject() = rule.testDispatcher.runBlockingTest{
        val habit2 = Habit(
            "begat",
            "lele",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )

        val habit3 = Habit(
            "begat",
            "lele",
            Habit.HabitType.GOOD,
            Habit.HabitPriority.HIGH,
            10,
            10,
            10
        )

        repository.addItem(habit)
        repository.addItem(habit2)
        repository.addItem(habit3)
        assertTrue(repository.getLocalData().first().size == 1)
    }
}