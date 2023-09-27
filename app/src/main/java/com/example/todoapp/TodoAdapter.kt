package com.example.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.query
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_todo.view.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class TodoAdapter(val list: List<TodoModel>): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent,false))
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(todoModel: TodoModel) {
            with(itemView){
                val colors = resources.getIntArray(R.array.random_color)
                val randomColor = colors[java.util.Random().nextInt(colors.size)]
                viewColorTag.setBackgroundColor(randomColor)
                tvTitle.text = todoModel.title
                tvSubTitle.text = todoModel.description
                tvCategory.text = todoModel.category
                updateTime(todoModel.time)
                updateDate(todoModel.date)
            }
        }

        private fun updateTime(time: Long) {
            // 4:15 am
            val myFormat="h:mm a"
            val sdf = SimpleDateFormat(myFormat)
            itemView.tvTime.text = sdf.format(Date(time))
        }

        private fun updateDate(time: Long) {
            //Mon, 14 Aug 2023
            val myFormat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myFormat)
            itemView.tvDate.text = sdf.format(Date(time))
        }
    }
}
