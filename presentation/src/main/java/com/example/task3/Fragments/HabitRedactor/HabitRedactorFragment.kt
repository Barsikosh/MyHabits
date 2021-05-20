package com.example.task3.Fragments.HabitRedactor

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.task3.*
import com.example.domain.entities.Habit
import com.example.task3.DI.MyApplication
import kotlinx.android.synthetic.main.redactor_fragment.*
import javax.inject.Inject

class HabitRedactorFragment : Fragment(), ColorPickerDialog.OnInputListener {

    companion object {
        const val ARGS_HABIT = "args_habit"
        const val ADD_HABIT = 1
        const val CHANGE_HABIT = 2
        const val COMMAND = "command"
    }

    @Inject
    lateinit var viewModel: RedactorHabitViewModel

    lateinit var colorDialog: DialogFragment;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       /* val addHabitUseCase = (requireActivity().application as MyApplication)
            .applicationComponent.getAddHabitUseCase()
        val updateHabitUseCase = (requireActivity().application as MyApplication)
            .applicationComponent.getUpdateHabitUseCase()*/

        (requireActivity().application as MyApplication).initViewModelRedactorComponent(this)
        (requireActivity().application as MyApplication).viewModelComponent.injectFragment2(this)

        /*viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RedactorHabitViewModel(addHabitUseCase, updateHabitUseCase) as T
            }
        }).get(RedactorHabitViewModel::class.java)*/

        return inflater.inflate(R.layout.redactor_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        color_button.backgroundTintList = ColorStateList.valueOf(viewModel.color)
        colorDialog = ColorPickerDialog()
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
            colorDialog.show(childFragmentManager, "Color Picker")
        }
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
        viewModel.color = habit.color
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

    private fun backToHabitList() {
        val navController = activity?.findNavController(R.id.my_nav_host_fragment)
        navController!!.navigate(R.id.action_redactor_to_viewPagerFragment)
    }

    private fun saveNewData() {
        if (checkAllProperties()) {
            val habit = collectHabit()
            viewModel.addHabit(habit)
            backToHabitList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveChangedData(habit: Habit) {
        if (checkAllProperties()) {
            val newHabit = collectHabit()
            if (habit.uid != null)
                newHabit.uid = habit.uid
            newHabit.date = habit.date
            if (newHabit.name != habit.name) {
                viewModel.addHabit(newHabit)
            }
            viewModel.updateHabit(newHabit)
            backToHabitList()
        }
    }

    private fun collectHabit(): Habit {
        return Habit(
            edit_habit_name.text.toString(), edit_description.text.toString(),
            Habit.HabitType.fromInt(radioGroup.indexOfChild(requireView().findViewById(radioGroup.checkedRadioButtonId))),
            Habit.HabitPriority.fromInt(spinner.selectedItemPosition),
            Integer.valueOf(edit_times.text.toString()),
            Integer.valueOf(edit_frequency.text.toString()),
            viewModel.color
        )
    }

    override fun sendColor(color: Int) {
        val state = ColorStateList.valueOf(color)
        color_button.backgroundTintList = state
        viewModel.color = color
    }
}