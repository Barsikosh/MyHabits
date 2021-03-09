package com.example.task3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable
import javax.security.auth.callback.Callback

class MainActivity : Activity() {

    companion object {
        const val RESULT_NEW_HABIT = 5
        const val RESULT_CHANGED_HABIT = 4
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onViewCreated()
        add_habit_button.setOnClickListener { addHabit() }
    }

    private fun onViewCreated() {
        habit_list.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = HabitAdapter(HabitData.getHabits()) { position ->
                changeHabit(position)
            }
        }
        val habitAdapter = habit_list.adapter as HabitAdapter
        val callback : ItemTouchHelper.Callback = MyItemTouchHelper(habitAdapter)
        val myItemTouchHelper = ItemTouchHelper(callback)
        myItemTouchHelper.attachToRecyclerView(habit_list)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val position = data?.getIntExtra(HabitRedactorActivity.POSITION, 0) ?: 0
        val habit = data?.getSerializableExtra(HabitRedactorActivity.HABIT) ?: null
        when (resultCode) {
            RESULT_NEW_HABIT -> {
                HabitData.addHabit(habit as Habit)
                habit_list.adapter?.notifyItemInserted(position)}
            RESULT_CHANGED_HABIT -> {
                HabitData.updateHabit(habit as Habit, position)
                habit_list.adapter?.notifyItemChanged(position)
            }
        }
    }

    private fun changeHabit(position: Int) {
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        newIntent.putExtra(HabitRedactorActivity.HABIT, HabitData.getHabit(position))
        newIntent.putExtra(HabitRedactorActivity.POSITION, position)
        startActivityForResult(newIntent, RESULT_CHANGED_HABIT)
    }

    private fun addHabit() {
        val newIntent = Intent(this, HabitRedactorActivity::class.java)
        newIntent.putExtra(HabitRedactorActivity.POSITION, HabitData.getSize())
        startActivityForResult(newIntent, RESULT_NEW_HABIT)
    }
}

