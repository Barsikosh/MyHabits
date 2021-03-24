package com.example.task3

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.task3.Fragments.HabitListFragment

class HabitPagerAdapter(activity: AppCompatActivity, private val fragments: List<Fragment>) : FragmentStateAdapter(activity) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): Fragment  = when(position){
        0 -> fragments[0]
        1 -> fragments[1]
        else -> throw Throwable()
    }
}