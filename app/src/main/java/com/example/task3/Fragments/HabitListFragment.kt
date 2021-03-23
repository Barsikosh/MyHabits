package com.example.task3.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task3.Adapter.HabitAdapter
import com.example.task3.Habit
import com.example.task3.HabitData
import com.example.task3.MyItemTouchHelper
import com.example.task3.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.habits_fragment.*
import kotlinx.android.synthetic.main.redactor_fragment.*

class HabitListFragment : Fragment() {

    companion object {

        const val HABIT_TYPE = "habit_type"

        fun newInstance(habitType: Habit.HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(HABIT_TYPE, habitType)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.habits_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_habit_button.setOnClickListener { addHabit() }

        val habitList = when (this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE)) {
            Habit.HabitType.GOOD -> HabitData.goodHabits
            else -> HabitData.badHabits
        }
        addAdapter(habitList)
    }

    private fun addHabit() {
        Navigation.createNavigateOnClickListener(R.id.action_gotoRedactor)
    }

    private fun updateAdapter() {

    }

    private fun addAdapter(habitList: MutableList<Habit>) {
        habit_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                HabitAdapter(habitList) { habit ->
                    changeHabit(habit)
                }
        }
        habit_list.adapter!!.notifyDataSetChanged()
        val habitAdapter = habit_list.adapter as HabitAdapter
        val callback: ItemTouchHelper.Callback = MyItemTouchHelper(habitAdapter)
        val myItemTouchHelper = ItemTouchHelper(callback)
        myItemTouchHelper.attachToRecyclerView(habit_list)
    }

    private fun changeHabit(habit: Habit) {
        val redactorFragment = HabitRedactorFragment.newInstance(habit)
        val transaction = childFragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.add(R.id.fragment_container, redactorFragment).commit()
    }
}