package com.example.task3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import androidx.core.view.get
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.second_activity.*
import com.example.task3.Habit.HabitType
import com.example.task3.Habit.HabitPriority


class HabitRedactorActivity : Activity() {

    companion object {
        const val HABIT = "habit"
        const val POSITION = "position"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        var habit = intent.getSerializableExtra(HABIT) ?: null
        var position = intent.getIntExtra(POSITION, 0)

        save_fab.setOnClickListener {
            if (habit != null) saveChangedData(habit as Habit, position)
            else saveNewData(position)
        }
        if (habit != null) updateText(habit as Habit)
    }

    private fun updateText(habit: Habit) {
        edit_habit_name.setText(habit.name)
        edit_description.setText(habit.description)
        radioGroup.check(habit.type.ordinal)
        spinner.setSelection(habit.priority.ordinal)
        edit_times.setText(habit.time)
        habit_period.setText(habit.period)
    }

    private fun checkAllProperties(): Boolean {

        return radioGroup.checkedRadioButtonId != -1 &&
                edit_description.text.isNotEmpty() &&
                edit_times.text.isNotEmpty() &&
                edit_frequency.text.isNotEmpty() &&
                edit_habit_name.text.isNotEmpty()
    }

    private fun saveNewData(position: Int) {
        if (checkAllProperties()) {
            val habit = collectHabit()
            val newIntent = Intent(this, MainActivity::class.java)
            newIntent.putExtra(HABIT, habit)
            newIntent.putExtra(POSITION, position)
            setResult(MainActivity.RESULT_NEW_HABIT, newIntent)
            finish()
        }

    }

    private fun collectHabit(): Habit{
        return Habit(edit_habit_name.text.toString(), edit_description.text.toString(),
            HabitType.fromInt(radioGroup.indexOfChild(findViewById(radioGroup.checkedRadioButtonId))),
            HabitPriority.fromInt(spinner.selectedItemPosition),
            Integer.valueOf(edit_times.text.toString()),
            Integer.valueOf(edit_frequency.text.toString()))
    }

    private fun saveChangedData(habit: Habit, position: Int) {
        var newHabit = collectHabit()
        val newIntent = Intent(this, MainActivity::class.java)
        newIntent.putExtra(HABIT, newHabit)
        newIntent.putExtra(POSITION, position)
        setResult(MainActivity.RESULT_CHANGED_HABIT, newIntent)
        finish()
    }
}