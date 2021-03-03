package com.example.task3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val habits = listOf(
        Habit("Чпокать Арину"),
        Habit("Курить Шмаль"),
        Habit("написать арине что она классная"),
        Habit("написать арине что она классная")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        onViewCreated()
        add_habit_button.setOnClickListener{addHabit()}
    }

    private fun onViewCreated() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HabitAdapter(habits)
        }

    }

    private fun addHabit(){
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        startActivity(newIntent)
    }

}