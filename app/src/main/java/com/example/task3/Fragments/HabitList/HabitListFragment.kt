package com.example.task3.Fragments.HabitList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task3.*
import com.example.task3.Adapters.HabitAdapter
import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.habits_fragment.*
import kotlinx.android.synthetic.main.redactor_fragment.*
import kotlinx.android.synthetic.main.view_pager.*


class HabitListFragment : Fragment(), LifecycleOwner {

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

    private lateinit var viewModel: HabitListViewModel



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val habitType =
            this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE) as Habit.HabitType

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HabitListViewModel(habitType) as T
            }
        }).get(HabitListViewModel::class.java)
        return inflater.inflate(R.layout.habits_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_habit_button.setOnClickListener { addHabit() }
        addAdapter()
        observeViewModels()
        val bottomSheet = BottomSheet()
        childFragmentManager.beginTransaction()
            .replace(R.id.containerBottomSheet, bottomSheet)
            .commit();
    }


    private fun observeViewModels() {
        viewModel.habits.observe(viewLifecycleOwner, Observer {
            it.let {
                (habit_list.adapter as HabitAdapter).refreshHabits(
                    it
                )
            }
        })
    }

    private fun addAdapter() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                HabitAdapter(
                    viewModel,
                    { habit ->
                        changeHabit(habit)
                    },
                    this@HabitListFragment.context
                )
        }
        habit_list.adapter!!.notifyDataSetChanged()
        val habitAdapter = habit_list.adapter as HabitAdapter
        val callback: ItemTouchHelper.Callback = MyItemTouchHelper(habitAdapter)
        val myItemTouchHelper = ItemTouchHelper(callback)
        myItemTouchHelper.attachToRecyclerView(habit_list)
    }

    private fun addHabit() {
        val navController = activity?.findNavController(R.id.my_nav_host_fragment)
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.COMMAND, HabitRedactorFragment.ADD_HABIT)
        navController!!.navigate(R.id.action_goto_redactor, bundle)
    }

    private fun changeHabit(habit: Habit) {
        val navController = activity?.findNavController(R.id.my_nav_host_fragment)
        val bundle = Bundle()
        bundle.putInt(HabitRedactorFragment.COMMAND, HabitRedactorFragment.CHANGE_HABIT)
        bundle.putSerializable(HabitRedactorFragment.ARGS_HABIT, habit)
        navController!!.navigate(R.id.action_goto_redactor, bundle)
    }
}