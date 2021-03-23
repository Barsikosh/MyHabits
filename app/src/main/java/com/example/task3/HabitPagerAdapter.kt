package com.example.task3

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.task3.Fragments.HabitListFragment

class HabitPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    val fragmentList =  mutableListOf<HabitListFragment>()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment  = when(position){
        0 -> HabitListFragment.newInstance(Habit.HabitType.GOOD)
        else -> HabitListFragment.newInstance(Habit.HabitType.BAD)
    }
}