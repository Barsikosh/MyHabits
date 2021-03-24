package com.example.task3.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.createNavigateOnClickListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task3.*
import com.example.task3.Adapter.HabitAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.habits_fragment.*
import kotlinx.android.synthetic.main.redactor_fragment.*
import kotlinx.android.synthetic.main.view_pager.*

class HabitListFragment : Fragment() {

    companion object {

        const val HABIT_TYPE = "habit_type"
        const val RESULT_NEW_HABIT = 5
        const val RESULT_CHANGED_HABIT = 4
        const val RESULT = "result"
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

        add_habit_button.setOnClickListener {addHabit()}
        val habitList = when (this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE)) {
            Habit.HabitType.GOOD -> HabitData.goodHabits
            else -> HabitData.badHabits
        }
        addAdapter(habitList)
    }

    private fun addHabit() {
        val navController = activity?.findNavController(R.id.my_nav_host_fragment)
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.COMMAND, HabitRedactorFragment.ADD_HABIT)
        navController!!.navigate(R.id.action_goto_redactor, bundle)
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
        val navController = activity?.findNavController(R.id.my_nav_host_fragment)
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.COMMAND, HabitRedactorFragment.CHANGE_HABIT)
        bundle.putSerializable(HabitRedactorFragment.ARGS_HABIT, habit)
        navController!!.navigate(R.id.action_goto_redactor, bundle)

    }
}