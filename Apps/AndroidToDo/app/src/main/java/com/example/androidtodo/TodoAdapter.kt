package com.example.androidtodo

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class TodoAdapter (
    private val todos: MutableList<Todo>
        ) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_todo,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val curTodo = todos[position]
//        holder.itemView.apply {
//            tvTodo
//        }
        holder.itemView.apply {
//            tvTodoTitle
            findViewById<TextView>(R.id.tvTodoTitle).text = curTodo.title
            findViewById<CheckBox>(R.id.cbDone).isChecked = curTodo.isChecked
        }
    }

    override fun getItemCount(): Int {
        return todos.size
    }
}