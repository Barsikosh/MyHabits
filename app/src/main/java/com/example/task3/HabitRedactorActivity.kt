package com.example.task3

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.second_activity.*
import com.example.task3.Habit.HabitType
import com.example.task3.Habit.HabitPriority


class HabitRedactorActivity : AppCompatActivity(), ColorPickerDialog.OnInputListener {

    companion object {
        const val HABIT = "habit"
        const val POSITION = "position"
        const val CHANGE_HABIT = 5
        const val ADD_HABIT = 4
        const val COMMAND = "command"
    }

    lateinit var colorDialog: DialogFragment;
    var color: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        color = resources.getColor(R.color.design_default_color_primary)
        onViewCreated()
    }

    private fun onViewCreated() {
        when (intent.getIntExtra(COMMAND, 0)) {

            ADD_HABIT -> save_fab.setOnClickListener { saveNewData() }
            CHANGE_HABIT -> {
                val habit = intent.getSerializableExtra(HABIT)
                val position = intent.getIntExtra(POSITION, 0)
                save_fab.setOnClickListener {
                    saveChangedData(position)
                    saveNewData()
                }
                updateText(habit as Habit)
            }
        }

        color_button.setOnClickListener {
            colorDialog.show(supportFragmentManager, "Color Picker")
        }
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
        color = habit.color
        val state = ColorStateList.valueOf(habit.color)
        color_button.backgroundTintList = state
    }

    private fun checkAllProperties(): Boolean {
        var result = true

        if (radioGroup.checkedRadioButtonId == -1){
            result = false
            habit_current_type.setTextColor(Color.RED)
        }

        if (edit_frequency.text.isEmpty() || edit_times.text.isEmpty()){
            result = false
            habit_frequency.setTextColor(Color.RED)
        }
        if (edit_habit_name.text.isEmpty()){
            result = false
            edit_habit_name.setHintTextColor(Color.RED)
        }
        return result
    }

    private fun saveNewData() {
        if (checkAllProperties()) {
            val habit = collectHabit()
            val newIntent = Intent(this, MainActivity::class.java)
            newIntent.putExtra(HABIT, habit)
            setResult(MainActivity.RESULT_NEW_HABIT, newIntent)
            finish()
        }
    }

    private fun saveChangedData(position: Int) {

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

        habit.color = color!!
        return habit
    }

    override fun sendColor(color: Int) {
        val state = ColorStateList.valueOf(color)
        color_button.backgroundTintList = state
        this.color = color
        colorDialog.dismiss()
    }
}