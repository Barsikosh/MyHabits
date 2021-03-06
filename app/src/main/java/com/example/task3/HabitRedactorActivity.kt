package com.example.task3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.habit_activity.*
import kotlinx.android.synthetic.main.list_item.*
import java.text.FieldPosition

class HabitRedactorActivity : Activity() {

    companion object {
        const val HABIT = "habit"
        const val POSITION = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var habit = intent.getSerializableExtra(HABIT) ?: Habit("da","da")
        var position = intent.getIntExtra(POSITION,0)
        setContentView(R.layout.habit_activity)
        save_button.setOnClickListener {
            saveData(habit as Habit, position)
        }
        updateText(habit as Habit)
    }

    private fun updateText(habit: Habit){
        if (habit != null){
            editHabitName.setText(habit.name)
            editHabitDescription.setText(habit.description)
        }
    }

    private fun saveData(habit: Habit, position: Int) {

        var newHabit = Habit(editHabitName.text.toString(), editHabitDescription.text.toString())
        val newIntent = Intent(this, MainActivity::class.java)
        newIntent.putExtra(HABIT, newHabit)
        newIntent.putExtra(POSITION, position)
        startActivity(newIntent)
    }


}