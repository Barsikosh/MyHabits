package com.example.task3.Adapter


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.task3.Habit
import com.example.task3.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.*
import kotlinx.android.synthetic.main.list_item.view.*

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

    override fun moveItem(oldPosition: Int, newPosition: Int){
        val habit = habits[oldPosition]
        habits[oldPosition] = habits[newPosition]
        habits[newPosition] = habit
        notifyItemMoved(oldPosition,newPosition)
    }

    override fun deleteItem(position: Int){
        habits.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class HabitViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }

        fun bind(habit: Habit) {
            habit_name.text = habit.name
            habit_description.text = habit.description
            habit_period.text = habit.time.toString() + " раз в " + habit.period.toString() + " дней"
            habit_priority.text = habit.priority.toString()
            habit_type.text = habit.type.toString()
            var shape = itemView.findViewById<View>(R.id.my_da).background as Drawable
            shape.setColorFilter(habit.color,PorterDuff.Mode.SRC_ATOP)
        }
    }
}