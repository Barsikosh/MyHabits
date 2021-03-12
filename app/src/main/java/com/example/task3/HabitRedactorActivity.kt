package com.example.task3

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.second_activity.*
import com.example.task3.Habit.HabitType
import com.example.task3.Habit.HabitPriority


class HabitRedactorActivity : AppCompatActivity(), ColorPickerDialog.OnInputListener {

    companion object {
        const val HABIT = "habit"
        const val POSITION = "position"
    }

    lateinit var colorDialog: DialogFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        onViewCreated()
    }

    private fun onViewCreated() {
        var habit = intent.getSerializableExtra(HABIT) ?: null
        var position = intent.getIntExtra(POSITION, 0)
        save_fab.setOnClickListener {
            if (habit != null) saveChangedData(habit as Habit, position)
            else saveNewData(position)
        }
        if (habit != null)
            updateText(habit as Habit)
        color_button.setOnClickListener {
            colorDialog.show(supportFragmentManager, "Color Picker") }
        colorDialog = ColorPickerDialog()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun updateText(habit: Habit) {
        edit_habit_name.setText(habit.name)
        edit_description.setText(habit.description)
        spinner.setSelection(habit.priority.value)
        edit_frequency.setText(habit.period.toString())
        edit_times.setText(habit.time.toString())
        when (habit.type) {
            HabitType.GOOD -> radioGroup.check(R.id.first_radio)
            HabitType.BAD -> radioGroup.check(R.id.second_radio)
        }
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

    private fun saveChangedData(habit: Habit, position: Int) {

        if (checkAllProperties()) {
            val newHabit = collectHabit()
            val newIntent = Intent(this, MainActivity::class.java)
            newIntent.putExtra(HABIT, newHabit)
            newIntent.putExtra(POSITION, position)
            setResult(MainActivity.RESULT_CHANGED_HABIT, newIntent)
            finish()
        }
    }

    private fun collectHabit(): Habit {
        val habit = Habit(
                edit_habit_name.text.toString(), edit_description.text.toString(),
                HabitType.fromInt(radioGroup.indexOfChild(findViewById(radioGroup.checkedRadioButtonId))),
                HabitPriority.fromInt(spinner.selectedItemPosition),
                Integer.valueOf(edit_times.text.toString()),
                Integer.valueOf(edit_frequency.text.toString())
        )
        var da =  color_button.background
        if (da is ColorDrawable){
            habit.color = da.color
        }
        return habit
    }

    override fun sendColor(color: Int) {
        color_button.setBackgroundColor(color)
        colorDialog.dismiss()
    }
}