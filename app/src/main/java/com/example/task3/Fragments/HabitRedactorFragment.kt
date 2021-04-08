package com.example.task3.Fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.task3.*
import kotlinx.android.synthetic.main.redactor_fragment.*

class HabitRedactorFragment: Fragment(), ColorPickerDialog.OnInputListener {

    companion object {
        const val ARGS_HABIT = "args_habit"
        const val ADD_HABIT = 1
        const val CHANGE_HABIT = 2
        const val COMMAND = "command"
        const val COLOR = "color"
    }

    var color: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.redactor_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color = resources.getColor(R.color.design_default_color_primary)
        when (arguments?.getInt(COMMAND)) {

            ADD_HABIT -> save_fab.setOnClickListener { saveNewData() }
            CHANGE_HABIT -> {
                val habit = requireArguments().getSerializable(ARGS_HABIT)
                save_fab.setOnClickListener {
                    saveChangedData(habit as Habit)
                }
                updateText(habit as Habit)
            }
            else -> throw IllegalArgumentException("Name required")
        }
        color_button.setOnClickListener {
            val navController = activity?.findNavController(R.id.my_nav_host_fragment)
            navController!!.navigate(R.id.action_redactor_to_colorPicker)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(COLOR, color)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null){
            color = savedInstanceState.getInt(COLOR)
            color_button.backgroundTintList = ColorStateList.valueOf(color)
        }
        super.onViewStateRestored(savedInstanceState)
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

        if (radioGroup.checkedRadioButtonId == -1) {
            result = false
            habit_current_type.setTextColor(Color.RED)
        }

        if (edit_frequency.text.isEmpty() || edit_times.text.isEmpty()) {
            result = false
            habit_frequency.setTextColor(Color.RED)
        }
        if (edit_habit_name.text.isEmpty()) {
            result = false
            edit_habit_name.setHintTextColor(Color.RED)
        }
        return result
    }

    private fun saveNewData() {
        if (checkAllProperties()) {
            val habit = collectHabit(null)
            HabitData.addHabit(habit)
            val navController = activity?.findNavController(R.id.my_nav_host_fragment)
            val bundle = Bundle()
            bundle.putInt(HabitListFragment.RESULT, HabitListFragment.RESULT_NEW_HABIT)
            bundle.putSerializable(ARGS_HABIT, habit)
            navController!!.navigate(R.id.action_redactor_to_viewPagerFragment, bundle)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveChangedData(habit: Habit) {

        if (checkAllProperties()) {
            val newHabit = collectHabit(habit.id)
            newHabit.id = habit.id;
            HabitData.updateHabit(newHabit, habit.id)
            val navController = activity?.findNavController(R.id.my_nav_host_fragment)
            val bundle = Bundle()
            bundle.putInt(HabitListFragment.RESULT, HabitListFragment.RESULT_CHANGED_HABIT)
            navController!!.navigate(R.id.action_redactor_to_viewPagerFragment, bundle)
        }
    }

    private fun collectHabit(id: Long?): Habit {
        return Habit(
            -1,
            edit_habit_name.text.toString(), edit_description.text.toString(),
            Habit.HabitType.fromInt(radioGroup.indexOfChild(requireView().findViewById(radioGroup.checkedRadioButtonId))),
            Habit.HabitPriority.fromInt(spinner.selectedItemPosition),
            Integer.valueOf(edit_times.text.toString()),
            Integer.valueOf(edit_frequency.text.toString()),
            this.color
        )
    }

    override fun sendColor(color: Int) {
        val state = ColorStateList.valueOf(color)
        color_button.backgroundTintList = state
        this.color = color
    }
}