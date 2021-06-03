package com.example.task3.UI

import android.graphics.Color
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.example.task3.MainActivity
import com.example.task3.R
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class RedactorUiTest : TestCase() {


    @get:Rule
    var addTraderActivity: IntentsTestRule<MainActivity> =
        IntentsTestRule(MainActivity::class.java)


    @Before
    fun setup() {
        onScreen<HabitListScreen> {
            addHabitButton.click()
        }
    }

    @Test
    fun checkAll() {
        onScreen<HabitListScreen> {
            habitName.isVisible()
            description.hasHint("Описание")
            habitName.hasHint("Название")
            prioritySpinner.isVisible()
            description.isVisible()
            saveFub.isVisible()
            habit_frequency.isVisible()
        }
    }

    @Test
    fun checkButtons() {
        onScreen<HabitListScreen> {
            colorButton.isClickable()
            saveFub.isClickable()
            prioritySpinner.isClickable()
            firstRadio.isClickable()
            secondRadio.isClickable()
        }
    }

    @Test
    fun testColorPicker() {
        onScreen<HabitListScreen> {
            colorButton.isVisible()
            colorButton.click()
            color.isVisible()
            color.click()
        }
    }

    @Test
    fun testChangeText() {
        onScreen<HabitListScreen> {
            habit_frequency.replaceText("100")
            habit_frequency.hasText("100")
            habitName.replaceText("begat")
            habitName.hasText("begat")
            description.replaceText("potom spat")
            description.hasText("potom spat")
        }
    }
}