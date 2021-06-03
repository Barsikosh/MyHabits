package com.example.task3.UI

import com.agoda.kakao.bottomnav.KBottomNavigationView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.example.task3.R
import kotlinx.android.synthetic.main.redactor_fragment.*

class HabitListScreen: Screen<HabitListScreen>() {


    val addHabitButton = KButton {
        withId(R.id.add_habit_button)
    }

    val habitName  = KEditText{
        withId(R.id.edit_habit_name)
    }

    val description  = KEditText{
        withId(R.id.edit_description)
    }

    val saveFub = KButton {
        withId(R.id.save_fab)
    }

    val colorButton = KButton{
        withId(R.id.color_button)
    }

    val color = KButton{
        withId(R.id.color_button1)
    }

    val habit_frequency = KEditText{
        withId(R.id.edit_frequency)
    }

    val prioritySpinner = KButton{
        withId(R.id.spinner)
    }

    val firstRadio = KButton{
        withId(R.id.first_radio)
    }

    val secondRadio = KButton{
        withId(R.id.second_radio)
    }
}