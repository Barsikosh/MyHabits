package com.example.task3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.task3.Fragments.HabitListFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.view_pager.*
import kotlinx.android.synthetic.main.view_pager.view.*
import java.lang.Exception


class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            HabitListFragment.newInstance(Habit.HabitType.GOOD),
            HabitListFragment.newInstance(Habit.HabitType.BAD)
        )

        val adapter = HabitPagerAdapter(
            activity as AppCompatActivity,
            fragmentList
        )

        view.viewPager.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        TabLayoutMediator(this.tablay, viewPager) { tab, position ->
            tab.text =  when(position){
                0 -> "Полезные"
                1 -> "Вредные"
                else -> throw Exception("no more habits")
            }
            viewPager.setCurrentItem(tab.position, true)
        }.attach()
    }

}