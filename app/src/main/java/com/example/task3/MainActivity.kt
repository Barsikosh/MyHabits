package com.example.task3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkUpdate()
        onViewCreated()
        add_habit_button.setOnClickListener { addHabit() }
    }

    private fun onViewCreated() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HabitAdapter(HabitData.getHabits()) { position, habit ->
                changeHabit(habit, position)
            }
        }
    }

    private fun checkUpdate(){
        var habit: Serializable? = intent.getSerializableExtra(HabitRedactorActivity.HABIT) ?: return
        habit = habit as Habit
        var position = intent.getIntExtra(HabitRedactorActivity.POSITION,0)
        if (position >= HabitData.getDataSize())
            HabitData.addHabit(habit)
        else HabitData.updateHabit(habit, position)
        updateData()
    }

    private fun updateData() {
        habit_list.adapter?.notifyDataSetChanged()
    }

    private fun changeHabit(habit: Habit, position: Int) {
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        newIntent.putExtra(HabitRedactorActivity.HABIT, habit)
        newIntent.putExtra(HabitRedactorActivity.POSITION, position)
        startActivity(newIntent)
    }

    private fun addHabit() {
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        newIntent.putExtra(HabitRedactorActivity.POSITION, HabitData.getDataSize())
        startActivity(newIntent)
    }
}

