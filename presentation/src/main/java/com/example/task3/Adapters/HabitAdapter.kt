package com.example.task3.Adapters

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.Habit
import com.example.task3.Fragments.HabitList.HabitListViewModel
import com.example.task3.MainActivity
import com.example.task3.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*

class HabitAdapter(
    private val viewModel: HabitListViewModel,
    private val onItemClick: ((Habit) -> Unit),
    private val habitExecution: ((Habit) -> Unit),
    private val context: Context?
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>(),
    ITouchHelperAdapter {

    private var habits: List<Habit> = viewModel.getItems() ?: emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HabitViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    override fun getItemCount(): Int = habits.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun moveItem(startPosition: Int, nextPosition: Int) {
        viewModel.habitsMoved(startPosition, nextPosition)
        notifyItemMoved(startPosition, nextPosition)
    }

    override fun deleteItem(position: Int) {
        viewModel.deleteHabit(habits[position])
        notifyItemRemoved(position)
    }

    fun refreshHabits(habitList: List<Habit>) {
        habits = habitList
        notifyDataSetChanged()
    }

    inner class HabitViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            containerView.setOnClickListener {
                onItemClick.invoke(habits[adapterPosition])
            }
            containerView.findViewById<Button>(R.id.accept_button).setOnClickListener {
                habitExecution.invoke(habits[adapterPosition])
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun bind(habit: Habit) {
            val resources = context!!.resources
            habit.name.also {
                containerView.habit_name.text = it
            }
            "${resources.getString(R.string.description)}: ${habit.description}".also {
                containerView.habit_description.text = it
            }
            "${resources.getString(R.string.repeat)} ${
                resources.getQuantityString(
                    R.plurals.count,
                    habit.time,
                    habit.time
                )
            }  ${resources.getString(R.string.`in`)} ${
                resources.getQuantityString(
                    R.plurals.times,
                    habit.period,
                    habit.period
                )
            }"
                .also { containerView.habit_period.text = it }
            "${MainActivity.CONTEXT.getString(R.string.priority_text)}: ${habit.priority}".also {
                containerView.habit_priority.text = it
            }
            val stateList = ColorStateList.valueOf(habit.color)
            containerView.card_view.backgroundTintList = stateList
        }
    }
}