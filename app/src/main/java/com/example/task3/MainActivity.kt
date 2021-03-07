package com.example.task3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : Activity() {

    companion object {
        const val RESULT_NEW_HABIT = 1
        const val RESULT_CHANGED_HABIT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onViewCreated()
        add_habit_button.setOnClickListener { addHabit() }
        updateData()
    }

    private fun onViewCreated() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HabitAdapter(HabitData.getHabits()) { position, habit ->
                changeHabit(habit, position)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val position = data?.getIntExtra(HabitRedactorActivity.POSITION, 0) ?: 0
        val habit = data?.getSerializableExtra(HabitRedactorActivity.HABIT) as Habit
        when (resultCode) {
            RESULT_NEW_HABIT -> HabitData.addHabit(habit)
            RESULT_CHANGED_HABIT -> HabitData.updateHabit(habit, position)
        }
        updateData()
    }

    private fun updateData() {
        habit_list.adapter?.notifyDataSetChanged()
    }

    private fun changeHabit(habit: Habit, position: Int) {
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        newIntent.putExtra(HabitRedactorActivity.HABIT, habit)
        newIntent.putExtra(HabitRedactorActivity.POSITION, position)
        startActivityForResult(newIntent, RESULT_CHANGED_HABIT)
    }

    private fun addHabit() {
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        newIntent.putExtra(HabitRedactorActivity.POSITION, HabitData.getDataSize())
        startActivityForResult(newIntent, RESULT_NEW_HABIT)
    }
}

