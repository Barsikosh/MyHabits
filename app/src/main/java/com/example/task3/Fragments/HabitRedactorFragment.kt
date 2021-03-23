package com.example.task3.Fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.task3.*
import kotlinx.android.synthetic.main.redactor_fragment.*

class HabitRedactorFragment: Fragment(), ColorPickerDialog.OnInputListener {

    companion object {

        const val ARGS_HABIT = "args_habit"
        const val ADD_HABIT = 1
        const val CHANGE_HABIT = 2
        const val COMMAND = "command"

        fun newInstance(habit: Habit?) : HabitRedactorFragment{
            val fragment = HabitRedactorFragment()
            val bundle = Bundle()
            return if (habit != null){
                bundle.putSerializable(ARGS_HABIT, habit)
                bundle.putInt(COMMAND, CHANGE_HABIT)
                fragment.arguments = bundle
                fragment
            } else{
                bundle.putInt(COMMAND, ADD_HABIT)
                fragment.arguments = bundle
                fragment
            }
        }
    }

    private lateinit var colorDialog: ColorPickerDialog;
    var color: Int =  R.color.design_default_color_primary

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.redactor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (arguments!!.getInt(COMMAND)) {

            ADD_HABIT -> save_fab.setOnClickListener { saveNewData() }
            CHANGE_HABIT -> {
                val habit = arguments!!.getSerializable(ARGS_HABIT)
                save_fab.setOnClickListener {
                    saveChangedData(habit as Habit)
                    saveNewData()
                }
                updateText(habit as Habit)
            }
        }

        color_button.setOnClickListener {
            colorDialog.show(childFragmentManager, "Color Picker")
        }
        colorDialog = ColorPickerDialog()
    }

    private fun updateText(habit: Habit) {
        edit_habit_name.setText(habit.name)
        edit_description.setText(habit.description)
        spinner.setSelection(habit.priority.value)
        edit_frequency.setText(habit.period.toString())
        edit_times.setText(habit.time.toString())
        when (habit.type) {
            Habit.HabitType.GOOD -> radioGroup.check(R.id.first_radio)
            Habit.HabitType.BAD -> radioGroup.check(R.id.second_radio)
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
            HabitData.addHabit(habit)
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    private fun saveChangedData(habit: Habit) {

        if (checkAllProperties()) {
            val newHabit = collectHabit()

        }
    }

    private fun collectHabit(): Habit {
        val habit = Habit(
            edit_habit_name.text.toString(), edit_description.text.toString(),
            Habit.HabitType.fromInt(radioGroup.indexOfChild(view!!.findViewById(radioGroup.checkedRadioButtonId))),
            Habit.HabitPriority.fromInt(spinner.selectedItemPosition),
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