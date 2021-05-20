package com.example.task3.DI

import android.app.Application
import com.example.domain.entities.Habit
import com.example.task3.DI.ViewModelComponent.HabitListViewModelComponent
import com.example.task3.DI.ViewModelComponent.ViewModelComponent
import com.example.task3.DI.ViewModelComponent.ListViewModelModule
import com.example.task3.DI.ViewModelComponent.RedactorViewModelModule
import com.example.task3.Fragments.HabitList.HabitListFragment
import com.example.task3.Fragments.HabitRedactor.HabitRedactorFragment

class MyApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent
    private set

    lateinit var viewModelComponent: ViewModelComponent

    lateinit var listViewModelComponent: HabitListViewModelComponent


    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()
    }

    fun initViewModelListComponent(habitListFragment: HabitListFragment, habitType: Habit.HabitType){
        listViewModelComponent = applicationComponent.getListViewModelComponent().requestModule(
            ListViewModelModule(habitListFragment,habitType))!!.build()!!
    }

    fun initViewModelRedactorComponent(habitRedactorFragment: HabitRedactorFragment){
        viewModelComponent = applicationComponent.getViewModelComponent().requestModule(
            RedactorViewModelModule(habitRedactorFragment))!!.build()!!
    }
}