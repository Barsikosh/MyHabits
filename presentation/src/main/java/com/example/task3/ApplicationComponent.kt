package com.example.task3

import com.example.task3.Fragments.HabitList.HabitListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [])
interface ApplicationComponent {

    fun inject(habitListFragment: HabitListFragment)
}