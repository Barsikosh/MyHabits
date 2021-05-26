package com.example.task3.Fragments.HabitList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.task3.*
import com.example.task3.Adapters.HabitAdapter
import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment
import com.example.domain.entities.Habit
import com.example.task3.Adapters.MyItemTouchHelper
import com.example.task3.DI.MyApplication
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import kotlinx.android.synthetic.main.habits_fragment.*
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.redactor_fragment.*
import kotlinx.android.synthetic.main.view_pager.*
import java.util.*
import javax.inject.Inject


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

    @Inject
    lateinit var viewModel: HabitListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val habitType =
            this@HabitListFragment.arguments?.getSerializable(HABIT_TYPE) as Habit.HabitType
        (requireActivity().application as MyApplication).initViewModelListComponent(this, habitType)
        (requireActivity().application as MyApplication).listViewModelComponent.injectListFragment(this)
        return inflater.inflate(R.layout.habits_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        add_habit_button.setOnClickListener {
            addHabit()
        }

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
                (habit_list.adapter as HabitAdapter).refreshHabits(it)
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

                    { habit ->
                        doneHabit(habit)
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

    private fun doneHabit(habit: Habit) {
        viewModel.postHabit(habit)
        val countsLeft = habit.time - habit.postDate(Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - 1)
        val text: String = if (habit.type == Habit.HabitType.GOOD) {
            if (countsLeft > 0) {
                "${getString(R.string.good_toast1)} ${
                    requireContext().resources.getQuantityString(
                        R.plurals.count,
                        countsLeft,
                        countsLeft
                    )
                }"
            } else
                getString(R.string.good_toast2)
        } else {
            if (countsLeft > 0) {
                "${getString(R.string.bad_toast1)} ${
                    requireContext().resources.getQuantityString(
                        R.plurals.count,
                        countsLeft,
                        countsLeft
                    )
                }"
            } else {
                getString(R.string.bad_toast2)
            }
        }
        val toast = Toast.makeText(requireActivity(), text, Toast.LENGTH_LONG)
        toast.show()
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