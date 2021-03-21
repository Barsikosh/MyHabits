package com.example.task3.Adapter


import android.graphics.PorterDuff

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.Habit
import com.example.task3.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*

class HabitAdapter(private val habits: MutableList<Habit>,
                   private val onItemClick: ((Int) -> Unit))
    : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>(),
    ITouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun moveItem(startPosition: Int, nextPosition: Int){
        val habit = habits[startPosition]
        habits[startPosition] = habits[nextPosition]
        habits[nextPosition] = habit
        notifyItemMoved(startPosition,nextPosition)
    }

    override fun deleteItem(position: Int){
        habits.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class HabitViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(adapterPosition)
            }
        }

        fun bind(habit: Habit) {
            habit_name.text = "Название: ${habit.name}"
            habit_description.text = "Описание: ${habit.description}"
            habit_period.text = "Повторять ${habit.time} раз в ${habit.period} дней"
            habit_priority.text =  habit.priority.toString()
            habit_type.text = habit.type.toString()
            val shape = itemView.findViewById<View>(R.id.my_shape).background
            shape.setColorFilter(habit.color,PorterDuff.Mode.SRC_ATOP)
        }
    }
}