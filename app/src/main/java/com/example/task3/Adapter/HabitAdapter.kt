package com.example.task3.Adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.Adapter.*
import com.example.task3.Habit
import com.example.task3.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.view.*


class HabitAdapter(
    private val habits: MutableList<Habit>,
    private val onItemClick: ((Habit) -> Unit),
    private val context: Context?
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>(),
    ITouchHelperAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun moveItem(startPosition: Int, nextPosition: Int) {
        val habit = habits[startPosition]
        habits[startPosition] = habits[nextPosition]
        habits[nextPosition] = habit
        notifyItemMoved(startPosition, nextPosition)
    }

    override fun deleteItem(position: Int) {
        habits.removeAt(position)
        notifyItemRemoved(position)
    }


    inner class HabitViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                onItemClick.invoke(habits[adapterPosition])
            }
        }

        fun bind(habit: Habit) {
            "Название: ${habit.name}".also { containerView.habit_name.text = it }
            "Описание: ${habit.description}".also { containerView.habit_description.text = it }
            "Повторять ${
                context!!.resources.getQuantityString(
                    R.plurals.count,
                    habit.time,
                    habit.time
                )
            } в ${
                context.resources.getQuantityString(
                    R.plurals.times,
                    habit.period,
                    habit.period
                )
            }"
                .also { containerView.habit_period.text = it }
            containerView.habit_priority.text = habit.priority.toString()
            containerView.habit_type.text = habit.type.toString()
            val shape = itemView.findViewById<View>(R.id.my_shape).background
            shape.setColorFilter(habit.color, PorterDuff.Mode.SRC_ATOP)
        }
    }
}